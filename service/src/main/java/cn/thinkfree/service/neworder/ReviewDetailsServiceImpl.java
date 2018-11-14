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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
                quotationVo.setBasicsTableData(getBasisConstruction(projectNo, room.getRoomType(),room.getRoomName()));
                //给软装赋值
                quotationVo.setMaterialTableData(getSoftQuote(projectNo, room.getRoomType(),room.getRoomName()));
                //给硬装赋值
                quotationVo.setFurnitureTableData(getHardQuote(projectNo, room.getRoomType(),room.getRoomName()));
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
        ProjectQuotationRoomsSoftDecoration decoration = new ProjectQuotationRoomsSoftDecoration();
        decoration.setProjectNo(softQuoteVO.getProjectNo());
        decoration.setRoomType(softQuoteVO.getRoomType());
        decoration.setRoomName(softQuoteVO.getRoomName());
        decoration.setBrand(softQuoteVO.getBrand());
        decoration.setMaterialName(softQuoteVO.getMaterialName());
        decoration.setModel(softQuoteVO.getModel());
        decoration.setSpec(softQuoteVO.getSpec());
        decoration.setUnitPrice(softQuoteVO.getUnitPrice());
        decoration.setUsedQuantity(softQuoteVO.getUsedQuantity());
        //硬件保价
        if (StringUtils.isBlank(softQuoteVO.getId())) {
            decoration.setStatus(1);
            decoration.setId(UUID.randomUUID().toString().replaceAll("-",""));
            int i = projectQuotationRoomsSoftConstructMapper.insertSelective(decoration);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("插入失败");
            }
        }else if (StringUtils.isNotBlank(softQuoteVO.getId())) {
            decoration.setId(softQuoteVO.getId());
            int i = projectQuotationRoomsSoftConstructMapper.updateByPrimaryKeySelective(decoration);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("修改失败");
            }
        }
        Boolean result = updateRoom(decoration.getProjectNo());
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
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> saveHardQuote(HardQuoteVO hardQuoteVO) {
        ProjectQuotationRoomsHardDecoration decoration = new ProjectQuotationRoomsHardDecoration();
        decoration.setProjectNo(hardQuoteVO.getProjectNo());
        decoration.setRoomType(hardQuoteVO.getRoomType());
        decoration.setRoomName(hardQuoteVO.getRoomName());
        decoration.setBrand(hardQuoteVO.getBrand());
        decoration.setMaterialName(hardQuoteVO.getMaterialName());
        decoration.setModel(hardQuoteVO.getModel());
        decoration.setSpec(hardQuoteVO.getSpec());
        decoration.setUnitPrice(hardQuoteVO.getUnitPrice());
        decoration.setUsedQuantity(hardQuoteVO.getUsedQuantity());
        //硬件保价
        if (StringUtils.isBlank(hardQuoteVO.getId())) {
            decoration.setStatus(1);
            decoration.setId(UUID.randomUUID().toString().replaceAll("-",""));
            int i = projectQuotationRoomsHardConstructMapper.insertSelective(decoration);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("插入失败");
            }
        }else if (StringUtils.isNotBlank(hardQuoteVO.getId())) {
            decoration.setId(hardQuoteVO.getId());
            int i = projectQuotationRoomsHardConstructMapper.updateByPrimaryKeySelective(decoration);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("修改失败");
            }
        }
        Boolean result = updateRoom(decoration.getProjectNo());
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
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> saveBasisConstructionVO(BasisConstructionVO basisConstructionVO) {
        ProjectQuotationRoomsConstruct construct = new ProjectQuotationRoomsConstruct();
        construct.setProjectNo(basisConstructionVO.getProjectNo());
        construct.setRoomType(basisConstructionVO.getRoomType());
        construct.setRoomName(basisConstructionVO.getRoomName());
        construct.setConstructCode(basisConstructionVO.getConstructCode());
        construct.setConstructName(basisConstructionVO.getConstructName());
        construct.setCustomerPrice(basisConstructionVO.getUnitPrice());
        construct.setUnitPrice(basisConstructionVO.getUnitPrice());
        construct.setUsedQuantity(basisConstructionVO.getUsedQuantity());
        //基础保价
        if (StringUtils.isBlank(basisConstructionVO.getId())) {
            construct.setStatus(1);
            construct.setId(UUID.randomUUID().toString().replaceAll("-",""));
            int i = projectQuotationRoomsConstructMapper.insertSelective(construct);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("插入失败");
            }
        }
        if (StringUtils.isNotBlank(basisConstructionVO.getId())) {
            construct.setId(basisConstructionVO.getId());
            int i = projectQuotationRoomsConstructMapper.updateByPrimaryKeySelective(construct);
            if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("修改失败");
            }
        }
        Boolean result = updateRoom(basisConstructionVO.getProjectNo());
        if(!result){
            return RespData.error("修改房间总表总价失败");
        }
        return RespData.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> delBasisConstruction(String id) {
        ProjectQuotationRoomsConstruct construct = projectQuotationRoomsConstructMapper.selectByPrimaryKey(id);
        if(construct == null){
            return RespData.error("无效的数据");
        }
        construct.setStatus(2);
        projectQuotationRoomsConstructMapper.updateByPrimaryKeySelective(construct);
        updateRoom(construct.getProjectNo());
        return RespData.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> delHardQuote(String id) {
        ProjectQuotationRoomsHardDecoration hardDecoration = projectQuotationRoomsHardConstructMapper.selectByPrimaryKey(id);
        if(hardDecoration == null){
            return RespData.error("无效的数据");
        }
        hardDecoration.setStatus(2);
        projectQuotationRoomsHardConstructMapper.updateByPrimaryKeySelective(hardDecoration);
        updateRoom(hardDecoration.getProjectNo());
        return RespData.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> delSoftQuote(String id) {
        ProjectQuotationRoomsSoftDecoration softDecoration = projectQuotationRoomsSoftConstructMapper.selectByPrimaryKey(id);
        if(softDecoration == null){
            return RespData.error("无效的数据");
        }
        softDecoration.setStatus(2);
        projectQuotationRoomsSoftConstructMapper.updateByPrimaryKeySelective(softDecoration);
        updateRoom(softDecoration.getProjectNo());
        return RespData.success();
    }



    /**
     * @return
     * @Author jiang
     * @Description 硬装保价详情
     * @Date
     * @Param
     **/
    public List<HardQuoteVO> getHardQuote(String projectNo, String roomType,String roomName) {
        ProjectQuotationRoomsHardDecorationExample hardDecorationExample = new ProjectQuotationRoomsHardDecorationExample();
        ProjectQuotationRoomsHardDecorationExample.Criteria hardCriteria = hardDecorationExample.createCriteria();
        hardCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        hardCriteria.andProjectNoEqualTo(projectNo);
        hardCriteria.andRoomTypeEqualTo(roomType);
        hardCriteria.andRoomNameEqualTo(roomName);
        List<ProjectQuotationRoomsHardDecoration> decorations = projectQuotationRoomsHardConstructMapper.selectByExample(hardDecorationExample);
        List<HardQuoteVO> listVo = new ArrayList<>();
        for(ProjectQuotationRoomsHardDecoration decoration : decorations){
            HardQuoteVO quoteVO = new HardQuoteVO();
            quoteVO.setUnitPrice(decoration.getUnitPrice());
            quoteVO.setUsedQuantity(decoration.getUsedQuantity());
            quoteVO.setBrand(decoration.getBrand());
            quoteVO.setModel(decoration.getModel());
            quoteVO.setProjectNo(decoration.getProjectNo());
            quoteVO.setRoomType(decoration.getRoomType());
            quoteVO.setSpec(decoration.getSpec());
            quoteVO.setMaterialName(decoration.getMaterialName());
            quoteVO.setId(decoration.getId());
            if(quoteVO.getUnitPrice() == null){
                quoteVO.setUnitPrice(BigDecimal.ZERO);
            }
            if(quoteVO.getUsedQuantity() == null){
                quoteVO.setUsedQuantity(0);
            }
            quoteVO.setTotalPrice(quoteVO.getUnitPrice().multiply(new BigDecimal(quoteVO.getUsedQuantity())));
            listVo.add(quoteVO);
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
    public List<SoftQuoteVO> getSoftQuote(String projectNo, String roomType,String roomName) {
        ProjectQuotationRoomsSoftDecorationExample softDecorationExample = new ProjectQuotationRoomsSoftDecorationExample();
        ProjectQuotationRoomsSoftDecorationExample.Criteria hardCriteria = softDecorationExample.createCriteria();
        hardCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        hardCriteria.andProjectNoEqualTo(projectNo);
        hardCriteria.andRoomTypeEqualTo(roomType);
        hardCriteria.andRoomNameEqualTo(roomName);
        List<ProjectQuotationRoomsSoftDecoration> decorations = projectQuotationRoomsSoftConstructMapper.selectByExample(softDecorationExample);
        List<SoftQuoteVO> listVo = new ArrayList<>();
        for(ProjectQuotationRoomsSoftDecoration decoration : decorations){
            SoftQuoteVO quoteVO = new SoftQuoteVO();
            quoteVO.setUnitPrice(decoration.getUnitPrice());
            quoteVO.setUsedQuantity(decoration.getUsedQuantity());
            quoteVO.setBrand(decoration.getBrand());
            quoteVO.setModel(decoration.getModel());
            quoteVO.setProjectNo(decoration.getProjectNo());
            quoteVO.setRoomType(decoration.getRoomType());
            quoteVO.setSpec(decoration.getSpec());
            quoteVO.setMaterialName(decoration.getMaterialName());
            quoteVO.setId(decoration.getId());
            if(quoteVO.getUnitPrice() == null){
                quoteVO.setUnitPrice(BigDecimal.ZERO);
            }
            if(quoteVO.getUsedQuantity() == null){
                quoteVO.setUsedQuantity(0);
            }
            quoteVO.setTotalPrice(quoteVO.getUnitPrice().multiply(new BigDecimal(quoteVO.getUsedQuantity())));
            listVo.add(quoteVO);
        }
        return listVo;
    }
    /**
     * @return
     * @Description 基础施工项详情
     * @Date
     * @Param
     **/
    public List<BasisConstructionVO> getBasisConstruction(String projectNo, String roomType,String roomName) {
        ProjectQuotationRoomsConstructExample constructExample = new ProjectQuotationRoomsConstructExample();
        ProjectQuotationRoomsConstructExample.Criteria hardCriteria = constructExample.createCriteria();
        hardCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        hardCriteria.andProjectNoEqualTo(projectNo);
        hardCriteria.andRoomTypeEqualTo(roomType);
        hardCriteria.andRoomNameEqualTo(roomName);
        List<ProjectQuotationRoomsConstruct> constructs = projectQuotationRoomsConstructMapper.selectByExample(constructExample);
        List<BasisConstructionVO> listVo = new ArrayList<>();
        for(ProjectQuotationRoomsConstruct construct : constructs){
            BasisConstructionVO constructionVO = new BasisConstructionVO();
            constructionVO.setProjectNo(construct.getProjectNo());
            constructionVO.setRoomType(construct.getRoomType());
            constructionVO.setUnitPrice(construct.getUnitPrice());
            constructionVO.setUsedQuantity(construct.getUsedQuantity());
            constructionVO.setConstructCode(construct.getConstructCode());
            constructionVO.setConstructName(construct.getConstructName());
            constructionVO.setId(construct.getId());
            if(constructionVO.getUnitPrice() == null){
                constructionVO.setUnitPrice(BigDecimal.ZERO);
            }
            if(constructionVO.getUsedQuantity() == null){
                constructionVO.setUsedQuantity(0);
            }
            constructionVO.setTotalPrice(constructionVO.getUnitPrice().multiply(new BigDecimal(constructionVO.getUsedQuantity())));
            listVo.add(constructionVO);
        }
        return listVo;
    }




    /**
     * 修改房屋相应类型的总报价
     *
     * @return
     */
    public Boolean updateRoom(String projectNo) {
        ProjectQuotationRoomsExample roomsExample = new ProjectQuotationRoomsExample();
        roomsExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<ProjectQuotationRooms> quotationRooms = projectQuotationRoomsMapper.selectByExample(roomsExample);
        if(quotationRooms.isEmpty()){
            return false;
        }
        ProjectQuotationRooms rooms = quotationRooms.get(0);
        BigDecimal sumCons = getSumCons(projectNo);
        BigDecimal sumSoft = getSumSoft(projectNo);
        BigDecimal sumHard = getSumHard(projectNo);
        rooms.setConstructsPrice(sumCons);
        rooms.setSoftMaterialPrice(sumSoft);
        rooms.setHardMaterialPrice(sumHard);
        rooms.setTotalPrice(sumCons.add(sumHard).add(sumSoft));
        projectQuotationRoomsMapper.updateByPrimaryKeySelective(rooms);
        return true;
    }

    private BigDecimal getSumCons(String projectNo) {
        ProjectQuotationRoomsConstructExample constructExample = new ProjectQuotationRoomsConstructExample();
        constructExample.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(1);
        List<ProjectQuotationRoomsConstruct> roomsConstructs = projectQuotationRoomsConstructMapper.selectByExample(constructExample);
        BigDecimal sumCons = BigDecimal.ZERO;
        for(ProjectQuotationRoomsConstruct roomsConstruct : roomsConstructs){
            if(roomsConstruct.getUnitPrice() == null || roomsConstruct.getUsedQuantity() == null){
                continue;
            }
            sumCons = sumCons.add(roomsConstruct.getUnitPrice().multiply(new BigDecimal(roomsConstruct.getUsedQuantity())));
        }
        return sumCons;
    }
    private BigDecimal getSumSoft(String projectNo) {
        ProjectQuotationRoomsSoftDecorationExample decorationExample = new ProjectQuotationRoomsSoftDecorationExample();
        decorationExample.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(1);
        List<ProjectQuotationRoomsSoftDecoration> roomsConstructs = projectQuotationRoomsSoftConstructMapper.selectByExample(decorationExample);
        BigDecimal sumCons = BigDecimal.ZERO;
        for(ProjectQuotationRoomsSoftDecoration roomsConstruct : roomsConstructs){
            if(roomsConstruct.getUnitPrice() == null || roomsConstruct.getUsedQuantity() == null){
                continue;
            }
            sumCons = sumCons.add(roomsConstruct.getUnitPrice().multiply(new BigDecimal(roomsConstruct.getUsedQuantity())));
        }
        return sumCons;
    }
    private BigDecimal getSumHard(String projectNo) {
        ProjectQuotationRoomsHardDecorationExample decorationExample = new ProjectQuotationRoomsHardDecorationExample();
        decorationExample.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(1);
        List<ProjectQuotationRoomsHardDecoration> roomsConstructs = projectQuotationRoomsHardConstructMapper.selectByExample(decorationExample);
        BigDecimal sumCons = BigDecimal.ZERO;
        for(ProjectQuotationRoomsHardDecoration roomsConstruct : roomsConstructs){
            if(roomsConstruct.getUnitPrice() == null || roomsConstruct.getUsedQuantity() == null){
                continue;
            }
            sumCons = sumCons.add(roomsConstruct.getUnitPrice().multiply(new BigDecimal(roomsConstruct.getUsedQuantity())));
        }
        return sumCons;
    }

    /**
     * 获取精准报价审核信息
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ProjectQuotationCheckVo> getCheckDetail(String projectNo) {
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
            return RespData.success(vo);
        }
        return RespData.error("暂无审核信息");
    }

    /**
     * 提交精准报价审核信息
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<String> addCheckDetail(String projectNo) {
        ProjectQuotationCheck check = new ProjectQuotationCheck();
        check.setSubmitTime(new Date());
        check.setProjectNo(projectNo);
        check.setSubmitTime(new Date());
        check.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
        int i = checkMapper.insertSelective(check);
        if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
            return RespData.error("插入失败");
        }
        return RespData.success();
    }

    @Override
    public MyRespBundle<String> reviewOffer(String projectNo, int result, String refuseReason) {
        //审核结果(1,通过 2,不通过)
        if(result != 1 && result != 2){
            return RespData.error("无效的审核状态");
        }
        if(result == 2 && StringUtils.isBlank(refuseReason)){
            return RespData.error("必须填写不通过原因");
        }
        ProjectQuotationCheckExample checkExample = new ProjectQuotationCheckExample();
        checkExample.createCriteria().andProjectNoEqualTo(projectNo).andCheckStatusEqualTo(1);
        List<ProjectQuotationCheck> checks = checkMapper.selectByExample(checkExample);
        if(checks.isEmpty()){
            return RespData.error("没有查询到提交审核记录");
        }
        ProjectQuotationCheck check = checks.get(0);
        check.setResult(result);
        //(1,审核中 2,审核失败 3,审核通过)
        if(result == 1){
            check.setCheckStatus(3);
        }else{
            check.setCheckStatus(2);
        }
        check.setRefuseReason(refuseReason);
        checkMapper.updateByPrimaryKeySelective(check);
        return RespData.success();
    }
}
