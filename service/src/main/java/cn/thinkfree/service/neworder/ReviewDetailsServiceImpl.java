package cn.thinkfree.service.neworder;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.pcvo.ProjectQuotationCheckVo;
import cn.thinkfree.database.pcvo.QuotationVo;
import cn.thinkfree.database.vo.BasisConstructionVO;
import cn.thinkfree.database.vo.HardQuoteVO;
import cn.thinkfree.database.vo.SoftQuoteVO;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.utils.BaseToVoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: jiang
 * @Date: 2018/11/13 11:46
 * @Description:
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ReviewDetailsServiceImpl implements ReviewDetailsService {
    @Autowired
    private ProjectQuotationRoomsHardDecorationMapper projectQuotationRoomsHardConstructMapper;
    @Autowired
    private ProjectQuotationRoomsSoftDecorationMapper projectQuotationRoomsSoftConstructMapper;
    @Autowired
    private ProjectQuotationRoomsConstructMapper projectQuotationRoomsConstructMapper;
    @Autowired
    private ProjectQuotationMapper projectQuotationMapper;
    @Autowired
    ProjectQuotationRoomsMapper projectQuotationRoomsMapper;
    @Autowired
    ProjectQuotationCheckMapper checkMapper;

    /**
     * 获取精准报价
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<QuotationVo>> getPriceDetail(String projectNo) {
        List<QuotationVo> quotationVoList = new ArrayList<>();
        ProjectQuotationRoomsExample roomsExample = new ProjectQuotationRoomsExample();
        ProjectQuotationRoomsExample.Criteria roomsCriteria = roomsExample.createCriteria();
        roomsCriteria.andProjectNoEqualTo(projectNo);
        roomsCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<ProjectQuotationRooms> projectQuotationRooms = projectQuotationRoomsMapper.selectByExample(roomsExample);
        if (projectQuotationRooms.size() > 0) {
            for (ProjectQuotationRooms room : projectQuotationRooms) {
                QuotationVo quotationVo = new QuotationVo();
                quotationVo.setName(room.getRoomName());
                quotationVo.setValue(room.getRoomType());
                quotationVo.setBasicsSumPrice(room.getConstructsPrice());
                quotationVo.setFurnitureSumPrice(room.getHardMaterialPrice());
                quotationVo.setMaterialSumPrice(room.getSoftMaterialPrice());
                //给基础施工项赋值
                quotationVo.setBasicsTableData(getBasisConstruction(projectNo, room.getRoomType()));
                //给软装赋值
                quotationVo.setMaterialTableData(getSoftQuote(projectNo, room.getRoomType()));
                //给硬装赋值
                quotationVo.setFurnitureTableData(getHardQuote(projectNo, room.getRoomType()));
                quotationVoList.add(quotationVo);
            }
        }
        return RespData.success(quotationVoList);
    }

    /**
     * @return
     * @Author jiang
     * @Description 新增软保价
     * @Date
     * @Param
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> saveSoftQuote(SoftQuoteVO softQuoteVO) {
        Integer money = 0;
        ProjectQuotationRoomsSoftDecoration projectQuotationRoomsSoftDecoration = BaseToVoUtils.getVo(softQuoteVO, ProjectQuotationRoomsSoftDecoration.class);
        ProjectQuotationRoomsSoftDecorationExample example = new ProjectQuotationRoomsSoftDecorationExample();
        ProjectQuotationRoomsSoftDecorationExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(softQuoteVO.getProjectNo());
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andRoomTypeEqualTo(softQuoteVO.getRoomType());
        //基础保价
        if (softQuoteVO.getIsAdd().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            projectQuotationRoomsSoftDecoration.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
            int i = projectQuotationRoomsSoftConstructMapper.insertSelective(projectQuotationRoomsSoftDecoration);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("插入失败");
            }
            money = softQuoteVO.getUsedQuantity() * softQuoteVO.getUnitPrice();
        }
        if (softQuoteVO.getIsDelete().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            money = softQuoteVO.getUsedQuantity() * softQuoteVO.getUnitPrice() * (-1);
            ProjectQuotationRoomsSoftDecoration hardDecoration = new ProjectQuotationRoomsSoftDecoration();
            hardDecoration.setStatus(ProjectDataStatus.INVALID_STATUS.getValue());
            int i = projectQuotationRoomsSoftConstructMapper.updateByExampleSelective(hardDecoration, example);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("删除失败");
            }
        }
        if (softQuoteVO.getIsEdit().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            int i = projectQuotationRoomsSoftConstructMapper.updateByExampleSelective(projectQuotationRoomsSoftDecoration, example);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("修改失败");
            }
            money = softQuoteVO.getUsedQuantity() * softQuoteVO.getUnitPrice();
        }
        Boolean result = updateRoom(money, 2, softQuoteVO.getProjectNo(), softQuoteVO.getRoomType());
        if(!result){
            return RespData.error("修改房间总表总价失败");
        }
        return RespData.success();

    }

    /**
     * @return
     * @Author jiang
     * @Description 新增硬保价
     * @Date
     * @Param
     **/
    @Override
    public MyRespBundle<String> saveHardQuote(HardQuoteVO hardQuoteVO) {
        Integer money = 0;
        ProjectQuotationRoomsHardDecoration projectQuotationRoomsHardConstruct = BaseToVoUtils.getVo(hardQuoteVO, ProjectQuotationRoomsHardDecoration.class);
        ProjectQuotationRoomsHardDecorationExample example = new ProjectQuotationRoomsHardDecorationExample();
        ProjectQuotationRoomsHardDecorationExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(hardQuoteVO.getProjectNo());
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andRoomTypeEqualTo(hardQuoteVO.getRoomType());
        //硬件保价
        if (hardQuoteVO.getIsAdd().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            projectQuotationRoomsHardConstruct.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
            money = hardQuoteVO.getUsedQuantity() * hardQuoteVO.getUnitPrice();
            int i = projectQuotationRoomsHardConstructMapper.insertSelective(projectQuotationRoomsHardConstruct);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("插入失败");
            }
        }
        if (hardQuoteVO.getIsDelete().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            money = hardQuoteVO.getUsedQuantity() * hardQuoteVO.getUnitPrice() * (-1);
            ProjectQuotationRoomsHardDecoration hardDecoration = new ProjectQuotationRoomsHardDecoration();
            hardDecoration.setStatus(ProjectDataStatus.INVALID_STATUS.getValue());
            int i = projectQuotationRoomsHardConstructMapper.updateByExampleSelective(hardDecoration, example);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("删除失败");
            }
        }
        if (hardQuoteVO.getIsEdit().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            money = hardQuoteVO.getUsedQuantity() * hardQuoteVO.getUnitPrice();
            int i = projectQuotationRoomsHardConstructMapper.updateByExampleSelective(projectQuotationRoomsHardConstruct, example);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("修改失败");
            }
        }
        Boolean result = updateRoom(money, 3, hardQuoteVO.getProjectNo(), hardQuoteVO.getRoomType());
        if(!result){
            return RespData.error("修改房间总表总价失败");
        }
        return RespData.success();
    }

    /**
     * @return
     * @Author jiang
     * @Description 新增基础保价
     * @Date
     * @Param
     **/
    @Override
    public MyRespBundle<String> saveBasisConstructionVO(BasisConstructionVO basisConstructionVO) {
        Integer money = 0;
        ProjectQuotationRoomsConstruct projectQuotationRoomsConstruct = BaseToVoUtils.getVo(basisConstructionVO, ProjectQuotationRoomsConstruct.class);
        ProjectQuotationRoomsConstructExample example = new ProjectQuotationRoomsConstructExample();
        ProjectQuotationRoomsConstructExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(basisConstructionVO.getProjectNo());
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andRoomTypeEqualTo(basisConstructionVO.getRoomType());
        //基础保价
        if (basisConstructionVO.getIsAdd().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            money = basisConstructionVO.getUsedQuantity() * basisConstructionVO.getUnitPrice();
            projectQuotationRoomsConstruct.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
            int i = projectQuotationRoomsConstructMapper.insertSelective(projectQuotationRoomsConstruct);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("插入失败");
            }
        }
        if (basisConstructionVO.getIsDelete().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            money = basisConstructionVO.getUsedQuantity() * basisConstructionVO.getUnitPrice() * (-1);
            ProjectQuotationRoomsConstruct hardDecoration = new ProjectQuotationRoomsConstruct();
            hardDecoration.setStatus(ProjectDataStatus.INVALID_STATUS.getValue());
            int i = projectQuotationRoomsConstructMapper.updateByExampleSelective(hardDecoration, example);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("删除失败");
            }
        }
        if (basisConstructionVO.getIsEdit().equals(ProjectDataStatus.BASE_STATUS.getValue())) {
            money = basisConstructionVO.getUsedQuantity() * basisConstructionVO.getUnitPrice();
            int i = projectQuotationRoomsConstructMapper.updateByExampleSelective(projectQuotationRoomsConstruct, example);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("修改失败");
            }
        }
        Boolean result = updateRoom(money, 1, basisConstructionVO.getProjectNo(), basisConstructionVO.getRoomType());
        if(!result){
            return RespData.error("修改房间总表总价失败");
        }
        return RespData.success();
    }


    /**
     * 获取精准报价审核结果
     *
     * @param projectNo
     * @return
     */
    public ProjectQuotationCheckVo getQuotationCheck(String projectNo) {
        ProjectQuotationCheckExample checkExample = new ProjectQuotationCheckExample();
        checkExample.setOrderByClause("submit_time desc");
        ProjectQuotationCheckExample.Criteria checkCriteria = checkExample.createCriteria();
        checkCriteria.andProjectNoEqualTo(projectNo);
        checkCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<ProjectQuotationCheck> projectQuotationChecks = checkMapper.selectByExample(checkExample);
        if (projectQuotationChecks.size() > 0) {
            ProjectQuotationCheck projectQuotationCheck = projectQuotationChecks.get(0);
            ProjectQuotationCheckVo vo = BaseToVoUtils.getVo(projectQuotationCheck, ProjectQuotationCheckVo.class);
            vo.setCheckNum(projectQuotationChecks.size());
            return vo;
        }
        return null;
    }

    /**
     * @return
     * @Author jiang
     * @Description 硬装保价详情
     * @Date
     * @Param
     **/
    @Override
    public List<HardQuoteVO> getHardQuote(String projectNo, String roomType) {
        ProjectQuotationRoomsHardDecorationExample hardDecorationExample = new ProjectQuotationRoomsHardDecorationExample();
        ProjectQuotationRoomsHardDecorationExample.Criteria hardCriteria = hardDecorationExample.createCriteria();
        hardCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        hardCriteria.andProjectNoEqualTo(projectNo);
        hardCriteria.andRoomTypeEqualTo(roomType);
        List<ProjectQuotationRoomsHardDecoration> projectQuotationRoomsHardDecorations = projectQuotationRoomsHardConstructMapper.selectByExample(hardDecorationExample);
        List<HardQuoteVO> listVo = BaseToVoUtils.getListVo(projectQuotationRoomsHardDecorations, HardQuoteVO.class);
        if (listVo.size() > 0) {
            for (HardQuoteVO vo : listVo) {
                vo.setTotalPrice(vo.getUnitPrice() * vo.getUsedQuantity());
            }
        }
        return listVo;
    }
    /**
     * @return
     * @Author jiang
     * @Description 软装保价详情
     * @Date
     * @Param
     **/
    @Override
    public List<SoftQuoteVO> getSoftQuote(String projectNo, String roomType) {
        ProjectQuotationRoomsSoftDecorationExample softDecorationExample = new ProjectQuotationRoomsSoftDecorationExample();
        ProjectQuotationRoomsSoftDecorationExample.Criteria hardCriteria = softDecorationExample.createCriteria();
        hardCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        hardCriteria.andProjectNoEqualTo(projectNo);
        hardCriteria.andRoomTypeEqualTo(roomType);
        List<ProjectQuotationRoomsSoftDecoration> projectQuotationRoomsSoftDecorations = projectQuotationRoomsSoftConstructMapper.selectByExample(softDecorationExample);
        List<SoftQuoteVO> listVo = BaseToVoUtils.getListVo(projectQuotationRoomsSoftDecorations, SoftQuoteVO.class);
        if (listVo.size() > 0) {
            for (SoftQuoteVO vo : listVo) {
                vo.setTotalPrice(vo.getUnitPrice() * vo.getUsedQuantity());
            }
        }
        return listVo;
    }
    /**
     * @return
     * @Description 基础施工项详情
     * @Date
     * @Param
     **/
    @Override
    public List<BasisConstructionVO> getBasisConstruction(String projectNo, String roomType) {
        ProjectQuotationRoomsConstructExample constructExample = new ProjectQuotationRoomsConstructExample();
        ProjectQuotationRoomsConstructExample.Criteria hardCriteria = constructExample.createCriteria();
        hardCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        hardCriteria.andProjectNoEqualTo(projectNo);
        hardCriteria.andRoomTypeEqualTo(roomType);
        List<ProjectQuotationRoomsConstruct> projectQuotationRoomsConstruct = projectQuotationRoomsConstructMapper.selectByExample(constructExample);
        List<BasisConstructionVO> listVo = BaseToVoUtils.getListVo(projectQuotationRoomsConstruct, BasisConstructionVO.class);
        return listVo;
    }




    /**
     * 修改房屋相应类型的总报价
     *
     * @param money
     * @param type  1,基础施工项  2,软装  3,硬装
     * @return
     */
    public Boolean updateRoom(Integer money, Integer type, String projectNo, String roomType) {
        ProjectQuotationRooms rooms = new ProjectQuotationRooms();
        ProjectQuotationRoomsExample roomsExample = new ProjectQuotationRoomsExample();
        ProjectQuotationRoomsExample.Criteria roomsCriteria = roomsExample.createCriteria();
        roomsCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        roomsCriteria.andProjectNoEqualTo(projectNo);
        roomsCriteria.andRoomTypeEqualTo(roomType);
        List<ProjectQuotationRooms> projectQuotationRooms = projectQuotationRoomsMapper.selectByExample(roomsExample);
        ProjectQuotationRooms room = projectQuotationRooms.get(0);
        if (type == 1) {
            rooms.setConstructsPrice(money + room.getConstructsPrice());
        }
        if (type == 2) {
            rooms.setSoftMaterialPrice(money + room.getConstructsPrice());
        }
        if (type == 3) {
            rooms.setHardMaterialPrice(money + room.getConstructsPrice());
        }
        int i = projectQuotationRoomsMapper.updateByExampleSelective(rooms, roomsExample);
        if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
            return false;
        }
        return true;
    }
}
