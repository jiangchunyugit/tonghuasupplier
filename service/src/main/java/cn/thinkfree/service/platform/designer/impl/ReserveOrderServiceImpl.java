package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.DesignerOrderMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.mapper.ReserveProjectMapper;
import cn.thinkfree.database.model.DesignerOrder;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.model.ReserveProject;
import cn.thinkfree.database.model.ReserveProjectExample;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.designer.ReserveOrderService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.platform.vo.ReserveProjectVo;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.utils.DateUtils;
import cn.thinkfree.service.utils.OrderNoUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author xusonghui
 * 预定的接口
 */
@Service
public class ReserveOrderServiceImpl implements ReserveOrderService {
    @Autowired
    private ReserveProjectMapper reserveProjectMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private DesignerOrderMapper DesignerOrderMapper;
    @Autowired
    private ProjectUserService userService;
    @Autowired
    private BasicsService basicsService;

    /**
     * 创建设计订单
     *
     * @param ownerName 业主姓名
     * @param phone     业主手机号
     * @param address   地址
     * @param source    来源
     * @param style     装修风格
     * @param budget    装修预算
     * @param acreage   装修面积
     */
    @Override
    public void createReserveOrder(
            String ownerName, String phone, String address, Integer source, Integer style, String provinceCode, String cityCode, String areaCode,
            String oldOrNew, String budget, String acreage, String userId, String designerId, String companyId) {
        // TODO 待转换订单创建
        ReserveProject reserveProject = new ReserveProject();
        reserveProject.setOwnerName(ownerName);
        reserveProject.setPhone(phone);
        reserveProject.setAddress(address);
        reserveProject.setSource(source);
        reserveProject.setStyle(style);
        reserveProject.setBudget(budget);
        reserveProject.setAcreage(acreage);
        reserveProject.setCreateUserId(userId);
        reserveProject.setCompanyId(companyId);
        reserveProject.setProvince(provinceCode);
        reserveProject.setCity(cityCode);
        reserveProject.setArea(areaCode);
        reserveProject.setOldOrNew(oldOrNew);
        reserveProject.setDesignerId(designerId);
        //1待转换，2已转换，3业主取消，4其他
        reserveProject.setState(1);
        reserveProject.setReserveNo(OrderNoUtils.getNo("REVO"));
        reserveProject.setReserveTime(new Date());
        reserveProjectMapper.insertSelective(reserveProject);
    }

    @Override
    public void closeReserveOrder(String reserveNo, int state, String reason) {
        if (state != 3 && state != 4) {
            throw new RuntimeException("无效的状态");
        }
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        reserveProjectExample.createCriteria().andReserveNoEqualTo(reserveNo);
        List<ReserveProject> reserveProjects = reserveProjectMapper.selectByExample(reserveProjectExample);
        if (reserveProjects.isEmpty()) {
            throw new RuntimeException("无效的订单");
        }
        ReserveProject reserveProject = new ReserveProject();
        reserveProject.setState(state);
        reserveProject.setReason(reason);
        reserveProjectMapper.updateByExample(reserveProject, reserveProjectExample);
    }

    @Override
    public PageVo<List<ReserveProjectVo>> queryReserveOrder(String ownerName, String phone, int pageSize, int pageIndex) {
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        ReserveProjectExample.Criteria criteria = reserveProjectExample.createCriteria();
        if (StringUtils.isNotBlank(ownerName)) {
            criteria.andOwnerNameLike("%" + ownerName + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            criteria.andPhoneLike("%" + phone + "%");
        }
        long total = reserveProjectMapper.countByExample(reserveProjectExample);
        PageHelper.startPage(pageIndex, pageSize);
        List<ReserveProject> reserveProjects = reserveProjectMapper.selectByExample(reserveProjectExample);
        PageVo<List<ReserveProjectVo>> pageVo = new PageVo<>();
        List<ReserveProjectVo> reserveProjectVos = new ArrayList<>();
        Map<String,String> provinceMap = basicsService.getProvince(ReflectUtils.getList(reserveProjects,"province").toArray(new String[]{}));
        Map<String,String> cityMap = basicsService.getCity(ReflectUtils.getList(reserveProjects,"city").toArray(new String[]{}));
        Map<String,String> areaMap = basicsService.getArea(ReflectUtils.getList(reserveProjects,"area").toArray(new String[]{}));
        for(ReserveProject reserveProject : reserveProjects){
            ReserveProjectVo reserveProjectVo = new ReserveProjectVo();
            getVo(reserveProject, reserveProjectVo, provinceMap, cityMap, areaMap);
            reserveProjectVos.add(reserveProjectVo);
        }
        pageVo.setData(reserveProjectVos);
        pageVo.setTotal(total);
        pageVo.setPageSize(pageSize);
        return pageVo;
    }

    @Override
    public ReserveProjectVo queryReserveOrderByNo(String reserveNo) {
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        reserveProjectExample.createCriteria().andReserveNoEqualTo(reserveNo);
        List<ReserveProject> reserveProjects = reserveProjectMapper.selectByExample(reserveProjectExample);
        if (reserveProjects.isEmpty()) {
            throw new RuntimeException("无效的订单");
        }
        ReserveProject reserveProject = reserveProjects.get(0);
        ReserveProjectVo reserveProjectVo = new ReserveProjectVo();
        Map<String,String> provinceMap = basicsService.getProvince(reserveProjectVo.getProvince());
        Map<String,String> cityMap = basicsService.getCity(reserveProjectVo.getCity());
        Map<String,String> areaMap = basicsService.getArea(reserveProjectVo.getArea());
        getVo(reserveProject, reserveProjectVo, provinceMap, cityMap, areaMap);
        return reserveProjectVo;
    }

    private void getVo(ReserveProject reserveProject, ReserveProjectVo reserveProjectVo, Map<String, String> provinceMap,
                       Map<String, String> cityMap, Map<String, String> areaMap) {
        reserveProjectVo.setProvinceName(provinceMap.get(reserveProjectVo.getProvince()));
        reserveProjectVo.setCityName(cityMap.get(reserveProjectVo.getCity()));
        reserveProjectVo.setAreaName(areaMap.get(reserveProjectVo.getArea()));
        reserveProjectVo.setOwnerName(reserveProject.getOwnerName());
        reserveProjectVo.setArea(reserveProject.getArea());
        reserveProjectVo.setCity(reserveProject.getCity());
        reserveProjectVo.setProvince(reserveProject.getProvince());
        reserveProjectVo.setAcreage(reserveProject.getAcreage());
        reserveProjectVo.setAddress(reserveProject.getAddress());
        reserveProjectVo.setBudget(reserveProject.getBudget());
        reserveProjectVo.setChangeTime(reserveProject.getChangeTime());
        reserveProjectVo.setCompanyId(reserveProject.getCompanyId());
        reserveProjectVo.setCreateUserId(reserveProject.getCreateUserId());
        reserveProjectVo.setDesignerId(reserveProject.getDesignerId());
        reserveProjectVo.setDesignOrderNo(reserveProject.getDesignOrderNo());
        reserveProjectVo.setOldOrNew(reserveProject.getOldOrNew());
        reserveProjectVo.setPhone(reserveProject.getPhone());
        reserveProjectVo.setProjectNo(reserveProject.getProjectNo());
        reserveProjectVo.setReserveTime(reserveProject.getReserveTime());
        reserveProjectVo.setReason(reserveProject.getReason());
        reserveProjectVo.setReserveNo(reserveProject.getReserveNo());
        reserveProjectVo.setSource(reserveProject.getSource());
        reserveProjectVo.setState(reserveProject.getState());
        reserveProjectVo.setStyle(reserveProject.getStyle());
        reserveProjectVo.setHuxing(reserveProject.getHuxing());
    }

    @Autowired
    private UserCenterService userCenterService;

    /**
     * @param reserveNo        待转换订单编号
     * @param companyId        设计公司ID
     * @param source           订单来源
     * @param huxing           房屋户型，1小区房，2别墅，3复式，4其他
     * @param roomNum          房屋个数
     * @param officeNum        客厅个数
     * @param toiletNum        卫生间个数
     * @param province         省份编码
     * @param city             城市编码
     * @param region           区编码
     * @param addressDetail    装修详细地址
     * @param style            装修风格
     * @param area             建筑面积
     * @param houseType        房屋类型，1新房，2老房
     * @param peopleNum        常住人口
     * @param planStartTime    计划装修开始时间
     * @param planEndTime      计划装修结束时间
     * @param decorationBudget 装修预算
     * @param balconyNum       阳台个数
     * @param ownerName        业主姓名
     * @param ownerPhone       业主手机号
     * @param designerId       设计师ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createProject(String reserveNo, String companyId, int source, int huxing, int roomNum, int officeNum, int toiletNum,
                              String province, String city, String region, String addressDetail, String style, int area, int houseType, int peopleNum, String planStartTime,
                              String planEndTime, int decorationBudget, int balconyNum, String ownerName, String ownerPhone, String designerId) {
        checkReserve(reserveNo);
        UserMsgVo msgVo = userCenterService.registerUser(ownerName, ownerPhone, true);
        if(msgVo == null){
            throw new RuntimeException("业主注册失败");
        }
        String ownerId = msgVo.getConsumerId();
        Project project = new Project();
        project.setProjectNo(OrderNoUtils.getNo("PN"));
        project.setStatus(1);
        if(companyId != null){
            project.setStage(DesignStateEnum.STATE_20.getState());
        }else{
            project.setStage(DesignStateEnum.STATE_1.getState());
        }
        project.setCreateTime(new Date());
        project.setReleaseTime(new Date());
        project.setOrderSource(source);
        project.setHouseHuxing(huxing);
        project.setHouseRoom(roomNum);
        project.setHouseOffice(officeNum);
        project.setHouseToilet(toiletNum);
        project.setProvince(province);
        project.setCity(city);
        project.setRegion(region);
        project.setAddressDetail(addressDetail);
        project.setStyle(style);
        project.setArea(area);
        project.setPeopleNo(peopleNum);
        project.setPlanStartTime(DateUtils.strToDate(planStartTime, "yyyy-MM-dd"));
        project.setPlanEndTime(DateUtils.strToDate(planEndTime,"yyyy-MM-dd"));
        project.setDecorationBudget(decorationBudget);
        project.setOwnerId(ownerId);
        project.setBalcony(balconyNum);
        project.setHouseType(houseType);
        projectMapper.insertSelective(project);
		DesignerOrder designerOrder = new DesignerOrder();
        designerOrder.setId(UUID.randomUUID().toString().replaceAll("-",""));
        designerOrder.setProjectNo(project.getProjectNo());
        designerOrder.setCreateTime(new Date());
        designerOrder.setOrderNo(OrderNoUtils.getNo("DO"));
        designerOrder.setOrderStage(DesignStateEnum.STATE_1.getState());
        designerOrder.setAppointmentTime(new Date());
        designerOrder.setStatus(1);
        designerOrder.setStyleType(style + "");
        if(companyId != null){
            designerOrder.setCompanyId(companyId);
            designerOrder.setOrderStage(DesignStateEnum.STATE_20.getState());
        }
        DesignerOrderMapper.insertSelective(designerOrder);
        if(StringUtils.isNotBlank(designerId)){
            userService.addUserId(designerOrder.getOrderNo(),project.getProjectNo(),designerId, RoleFunctionEnum.DESIGN_POWER);
        }
        if(StringUtils.isNotBlank(ownerId)){
            userService.addUserId(designerOrder.getOrderNo(),project.getProjectNo(),ownerId, RoleFunctionEnum.OWNER_POWER);
        }
        //TODO 待创建施工订单
        if (StringUtils.isBlank(reserveNo)) {
            return;
        }
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        reserveProjectExample.createCriteria().andReserveNoEqualTo(reserveNo);
        ReserveProject reserveProject = new ReserveProject();
        reserveProject.setProjectNo(project.getProjectNo());
        reserveProject.setDesignOrderNo(designerOrder.getOrderNo());
        reserveProject.setChangeTime(new Date());
        reserveProject.setState(2);
        reserveProjectMapper.updateByExampleSelective(reserveProject, reserveProjectExample);
    }

    private void checkReserve(String reserveNo){
        if(StringUtils.isBlank(reserveNo)){
            return;
        }
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        reserveProjectExample.createCriteria().andReserveNoEqualTo(reserveNo);
        List<ReserveProject> projects = reserveProjectMapper.selectByExample(reserveProjectExample);
        if(projects.isEmpty()){
            throw new RuntimeException("没有查询到该订单");
        }
        if(projects.get(0).getState() == null || projects.get(0).getState() != 1){
            throw new RuntimeException("待转换订单状态不合法");
        }
    }
}
