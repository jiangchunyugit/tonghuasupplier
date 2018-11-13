package cn.thinkfree.service.neworder;

import cn.thinkfree.database.mapper.ProjectQuotationMapper;
import cn.thinkfree.database.mapper.ProjectQuotationRoomsConstructMapper;
import cn.thinkfree.database.mapper.ProjectQuotationRoomsHardDecorationMapper;
import cn.thinkfree.database.mapper.ProjectQuotationRoomsSoftDecorationMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.BasisConstructionVO;
import cn.thinkfree.database.vo.HardQuoteVO;
import cn.thinkfree.database.vo.SoftQuoteVO;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.utils.BaseToVoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        if(listVo.size() >0){
            for(HardQuoteVO vo :listVo){
                vo.setTotalPrice(vo.getUnitPrice()*vo.getUsedQuantity());
            }
        }
        return listVo;
    }

    @Override
    public List<SoftQuoteVO> getSoftQuote(String projectNo, String roomType) {
        ProjectQuotationRoomsSoftDecorationExample softDecorationExample = new ProjectQuotationRoomsSoftDecorationExample();
        ProjectQuotationRoomsSoftDecorationExample.Criteria hardCriteria = softDecorationExample.createCriteria();
        hardCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        hardCriteria.andProjectNoEqualTo(projectNo);
        hardCriteria.andRoomTypeEqualTo(roomType);
        List<ProjectQuotationRoomsSoftDecoration> projectQuotationRoomsSoftDecorations = projectQuotationRoomsSoftConstructMapper.selectByExample(softDecorationExample);
        List<SoftQuoteVO> listVo = BaseToVoUtils.getListVo(projectQuotationRoomsSoftDecorations, SoftQuoteVO.class);
        if(listVo.size() >0){
            for(SoftQuoteVO vo :listVo){
                vo.setTotalPrice(vo.getUnitPrice()*vo.getUsedQuantity());
            }
        }
        return listVo;
    }

    @Override
    public List<BasisConstructionVO> getBasisConstruction(String projectNo, String roomType) {
        ProjectQuotationRoomsConstructExample constructExample = new ProjectQuotationRoomsConstructExample();
        ProjectQuotationRoomsConstructExample.Criteria hardCriteria = constructExample.createCriteria();
        hardCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        hardCriteria.andProjectNoEqualTo(projectNo);
        hardCriteria.andRoomTypeEqualTo(roomType);
        List<ProjectQuotationRoomsConstruct> projectQuotationRoomsConstruct = projectQuotationRoomsConstructMapper.selectByExample(constructExample);
        List<BasisConstructionVO> listVo = BaseToVoUtils.getListVo(projectQuotationRoomsConstruct, BasisConstructionVO.class);
        if(listVo.size() >0){
            for(BasisConstructionVO vo :listVo){
                vo.setTotalPrice(vo.getUnitPrice()*vo.getUsedQuantity());
            }
        }
        return listVo;
    }

    /**
     * @Author jiang
     * @Description 软保价
     * @Date
     * @Param
     * @return
     **/
    @Override
    public List<SoftQuoteVO> saveSoftQuote(SoftQuoteVO softQuoteVO) {
        ProjectQuotationRoomsSoftDecorationExample projectQuotationRoomsSoftConstructExample = new ProjectQuotationRoomsSoftDecorationExample();
        ProjectQuotationRoomsSoftDecoration projectQuotationRoomsSoftConstruct = new ProjectQuotationRoomsSoftDecoration();
        //软保价
        projectQuotationRoomsSoftConstruct.setProjectNo(softQuoteVO.getProjectNo());
        projectQuotationRoomsSoftConstruct.setRoomType(softQuoteVO.getRoomType());
        projectQuotationRoomsSoftConstruct.setBrand(softQuoteVO.getBrand());
        projectQuotationRoomsSoftConstruct.setModel(softQuoteVO.getModel());
        projectQuotationRoomsSoftConstruct.setSpec(softQuoteVO.getSpec());
        projectQuotationRoomsSoftConstruct.setUnitPrice(softQuoteVO.getUnitPrice());
        projectQuotationRoomsSoftConstruct.setUsedQuantity(softQuoteVO.getUsedQuantity());
        projectQuotationRoomsSoftConstruct.setStatus(1);
        projectQuotationRoomsSoftConstructMapper.insertSelective(projectQuotationRoomsSoftConstruct);

  /*      //总价
        ProjectQuotation projectQuotation = new ProjectQuotation();
        projectQuotation.setProjectNo(softQuoteVO.getProjectNo());
        projectQuotation.setSoftDecorationPrice(new BigDecimal(softQuoteVO.getTotalPrice()));
        projectQuotationMapper.insertSelective(projectQuotation);*/
        return null;
    }
    /**
     * @Author jiang
     * @Description 硬保价
     * @Date
     * @Param
     * @return
     **/
    @Override
    public List<HardQuoteVO> saveHardQuote(HardQuoteVO hardQuoteVO) {
        ProjectQuotationRoomsHardDecorationExample projectQuotationRoomsHardConstructExample = new ProjectQuotationRoomsHardDecorationExample();
        ProjectQuotationRoomsHardDecoration projectQuotationRoomsHardConstruct = new ProjectQuotationRoomsHardDecoration();
        //软保价
        projectQuotationRoomsHardConstruct.setProjectNo(hardQuoteVO.getProjectNo());
        projectQuotationRoomsHardConstruct.setRoomType(hardQuoteVO.getRoomType());
        projectQuotationRoomsHardConstruct.setBrand(hardQuoteVO.getBrand());
        projectQuotationRoomsHardConstruct.setModel(hardQuoteVO.getModel());
        projectQuotationRoomsHardConstruct.setSpec(hardQuoteVO.getSpec());
        projectQuotationRoomsHardConstruct.setUnitPrice(hardQuoteVO.getUnitPrice());
        projectQuotationRoomsHardConstruct.setUsedQuantity(hardQuoteVO.getUsedQuantity());
        projectQuotationRoomsHardConstruct.setStatus(1);
        projectQuotationRoomsHardConstructMapper.insertSelective(projectQuotationRoomsHardConstruct);
        return null;
    }
/**
 * @Author jiang
 * @Description 新增基础保价
 * @Date
 * @Param
 * @return
 **/
    @Override
    public List<BasisConstructionVO> saveBasisConstructionVO(BasisConstructionVO basisConstructionVO) {
        ProjectQuotationRoomsConstructExample projectQuotationRoomsConstructExample = new ProjectQuotationRoomsConstructExample();
        ProjectQuotationRoomsConstruct projectQuotationRoomsConstruct = new ProjectQuotationRoomsConstruct();
        //软保价
        projectQuotationRoomsConstruct.setProjectNo(basisConstructionVO.getProjectNo());
        projectQuotationRoomsConstruct.setRoomType(basisConstructionVO.getRoomType());
        projectQuotationRoomsConstruct.setConstructName(basisConstructionVO.getConstructName());
        projectQuotationRoomsConstruct.setConstructCode(basisConstructionVO.getConstructCode());
        projectQuotationRoomsConstruct.setUnitPrice(basisConstructionVO.getUnitPrice());
        projectQuotationRoomsConstruct.setUsedQuantity(basisConstructionVO.getUsedQuantity());
        projectQuotationRoomsConstruct.setStatus(1);
        projectQuotationRoomsConstructMapper.insertSelective(projectQuotationRoomsConstruct);
        return null;
    }


}
