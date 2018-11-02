package cn.thinkfree.service.newproject;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.pcvo.*;
import cn.thinkfree.database.vo.OrderDetailsVO;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.constants.UserStatus;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.MathUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 项目相关
 *
 * @author gejiaming
 */
@Service
public class NewProjectServiceImpl implements NewProjectService {
    @Autowired
    OrderUserMapper orderUserMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    NewOrderService newOrderService;
    @Autowired
    ProjectDataMapper projectDataMapper;
    @Autowired
    DesignerOrderMapper designerOrderMapper;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    OrderApplyRefundMapper orderApplyRefundMapper;
    @Autowired
    NewOrderUserService newOrderUserService;
    @Autowired
    ProjectStageLogMapper projectStageLogMapper;


    /**
     * 项目列表
     *
     * @param appProjectSEO
     * @return
     */
    @Override
    public MyRespBundle<PageInfo<ProjectVo>> getAllProject(AppProjectSEO appProjectSEO) {
        OrderUserExample example1 = new OrderUserExample();
        OrderUserExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andUserIdEqualTo(appProjectSEO.getUserId());
        PageHelper.startPage(appProjectSEO.getPage(), appProjectSEO.getRows());
        PageInfo<ProjectVo> pageInfo = new PageInfo<>();
        //查询此人名下所有项目
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(example1);
        List<String> list = new ArrayList<>();
        for (OrderUser orderUser : orderUsers) {
            list.add(orderUser.getProjectNo());
        }
        //根据项目编号查询项目信息
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoIn(list);
        List<Project> projects = projectMapper.selectByExample(example);
        List<ProjectVo> projectVoList = new ArrayList<>();
        for (Project project : projects) {
            ProjectVo projectVo = BaseToVoUtils.getVo(project, ProjectVo.class, BaseToVoUtils.getProjectMap());
            if (projectVo == null) {
                System.out.println("工具类转换失败!!");
                return RespData.error("工具类转换失败!!");
            }
            //添加进度信息
            projectVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
            //添加业主信息
            PersionVo owner = new PersionVo();
//            Map userName = newOrderUserService.getUserName(project.getOwnerId(),ProjectDataStatus.OWNER.getDescription() );//正式时打开
            Map userName = newOrderUserService.getUserName("CC1810301612170000C", "CC");
            owner.setPhone(userName.get("phone").toString());
            owner.setName(userName.get("nickName").toString());
            projectVoList.add(projectVo);
        }
        pageInfo.setList(projectVoList);
        return RespData.success(pageInfo);
    }

    /**
     * 获取项目详情接口
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ProjectVo> getAppProjectDetail(String projectNo) {
        List<ProjectOrderDetailVo> projectOrderDetailVoList = new ArrayList<>();
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return RespData.error("项目不存在!!");
        }
        ProjectVo projectVo = BaseToVoUtils.getVo(projects.get(0), ProjectVo.class, BaseToVoUtils.getProjectMap());
        DesignerOrderExample designerOrderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria designCriteria = designerOrderExample.createCriteria();
        designCriteria.andProjectNoEqualTo(projectNo);
        designCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(designerOrderExample);
        DesignerOrder designerOrder = designerOrders.get(0);
        ProjectOrderDetailVo designerOrderDetailVo = BaseToVoUtils.getVo(designerOrder, ProjectOrderDetailVo.class);
//        ProjectOrderDetailVo designerOrderDetailVo = designerOrderMapper.selectByProjectNo(projectNo);
        //存放阶段信息
        List<OrderTaskSortVo> orderTaskSortVoList = new ArrayList<>();
        List<Map<String, Object>> maps = DesignStateEnum.allStates(ProjectDataStatus.PLAY_CONSUMER.getValue());
        for (Map<String, Object> map : maps) {
            OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
            orderTaskSortVo.setSort((Integer) map.get("key"));
            orderTaskSortVo.setName(map.get("val").toString());
            orderTaskSortVoList.add(orderTaskSortVo);
        }
        designerOrderDetailVo.setOrderTaskSortVoList(orderTaskSortVoList);
        designerOrderDetailVo.setTaskStage(projects.get(0).getStage());
        //存放订单类型
        designerOrderDetailVo.setOrderType(ProjectDataStatus.DESIGN_STATUS.getValue());
        //存放展示信息
        OrderPlayVo designOrderPlayVo = designerOrderMapper.selectByProjectNoAndStatus(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        List<PersionVo> persionList = new ArrayList<>();
        PersionVo persionVo = employeeMsgMapper.selectByUserId(designerOrder.getUserId());
        Map userName = newOrderUserService.getUserName(designerOrder.getUserId(), persionVo.getRole());//正式时打开
//        Map userName = newOrderUserService.getUserName("CC1810301612170000C", "CC");
        persionVo.setPhone(userName.get("phone").toString());
        persionList.add(persionVo);
        designOrderPlayVo.setPersionList(persionList);
        designerOrderDetailVo.setOrderPlayVo(designOrderPlayVo);
        ProjectOrderDetailVo constructionOrderDetailVo = constructionOrderMapper.selectByProjectNo(projectNo);
        List<OrderTaskSortVo> orderTaskSortVoList1 = new ArrayList<>();
        List<Map<String, Object>> maps1 = ConstructionStateEnum.allStates(ProjectDataStatus.PLAY_CONSUMER.getValue());
        for (Map<String, Object> map : maps1) {
            OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
            orderTaskSortVo.setSort((Integer) map.get("key"));
            orderTaskSortVo.setName(map.get("val").toString());
            orderTaskSortVoList1.add(orderTaskSortVo);
        }
        constructionOrderDetailVo.setOrderTaskSortVoList(orderTaskSortVoList1);
        constructionOrderDetailVo.setTaskStage(projects.get(0).getStage());
        constructionOrderDetailVo.setTaskStage(orderTaskSortVoList1.get(1).getSort());
        //存放订单类型
        constructionOrderDetailVo.setOrderType(ProjectDataStatus.CONSTRUCTION_STATUS.getValue());
        //存放展示信息
        OrderPlayVo constructionOrderPlayVo = constructionOrderMapper.selectByProjectNoAndStatus(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        constructionOrderPlayVo.setSchedule(DateUtil.daysCalculate(projects.get(0).getPlanStartTime(), projects.get(0).getPlanEndTime()));
        //存放人员信息
        List<PersionVo> constructionPersionList = employeeMsgMapper.selectAllByUserId(designerOrder.getUserId());
        constructionOrderPlayVo.setPersionList(constructionPersionList);
        constructionOrderDetailVo.setOrderPlayVo(constructionOrderPlayVo);
        projectOrderDetailVoList.add(designerOrderDetailVo);
        projectOrderDetailVoList.add(constructionOrderDetailVo);
        projectVo.setProjectOrderDetailVoList(projectOrderDetailVoList);
        return RespData.success(projectVo);
    }

    /**
     * 获取Pc端项目详情
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<PcProjectDetailVo> getPcProjectDetail(String projectNo) {
        PcProjectDetailVo pcProjectDetailVo = new PcProjectDetailVo();
        //获取项目阶段信息
        List<OrderTaskSortVo> orderTaskSortVoList = projectStageLogMapper.selectByProjectNo(projectNo);
        //组合施工订单信息
        ConstructionOrderVO constructionOrderVO;
        //组合设计订单信息
        DesignerOrderVo designerOrderVo;
        //组合报价信息
        OfferVo offerVo;
        //组合合同信息
        ContractVo contractVo;
        //组合施工信息
        SchedulingVo schedulingVo;
        //组合结算信息
        SettlementVo settlementVo;
        //组合评价管理
        EvaluateVo evaluateVo;
        //组合发票管理
        InvoiceVo invoiceVo;


        return null;
    }

    /**
     * 获取设计资料
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<DataVo> getDesignData(String projectNo) {
        DataVo dataVo = new DataVo();
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andTypeEqualTo(ProjectDataStatus.DESIGN_STATUS.getValue());
        List<ProjectData> projectDataList = projectDataMapper.selectByExample(example);
        List<DataDetailVo> dataDetailVoList = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (ProjectData projectData : projectDataList) {
            set.add(projectData.getCategory());
        }
        for (Integer integer : set) {
            DataDetailVo detailVo = new DataDetailVo();
            List<UrlDetailVo> urlList = new ArrayList<>();
            List<String> urlStringList = new ArrayList<>();
            for (ProjectData projectData : projectDataList) {
                if (projectData.getCategory().equals(integer)) {
                    if (projectData.getFileType().equals(ProjectDataStatus.FILE_PNG.getValue())) {
                        UrlDetailVo urlDetailVo = new UrlDetailVo();
                        detailVo.setConfirm(projectData.getIsConfirm());
                        detailVo.setCategory(projectData.getCategory());
                        detailVo.setUploadTime(projectData.getUploadTime());
                        urlDetailVo.setUrl(projectData.getUrl());
                        urlList.add(urlDetailVo);
                        urlStringList.add(projectData.getUrl());
                    }
                    if (projectData.getFileType().equals(ProjectDataStatus.FILE_THIRD.getValue())) {
                        detailVo.setThirdUrl(projectData.getUrl());
                    }
                    if (projectData.getFileType().equals(ProjectDataStatus.FILE_PDF.getValue())) {
                        detailVo.setPdfUrl(projectData.getUrl());
                    }
                }
            }
            detailVo.setUrlList(urlList);
            detailVo.setUrlStringList(urlStringList);
            dataDetailVoList.add(detailVo);
        }
        dataVo.setDataList(dataDetailVoList);
        return RespData.success(dataVo);
    }

    /**
     * 获取施工资料
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<UrlDetailVo>> getConstructionData(String projectNo) {
        List<UrlDetailVo> urlList = new ArrayList<>();
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andTypeEqualTo(ProjectDataStatus.CONSTRUCTION_STATUS.getValue());
        List<ProjectData> projectDataList = projectDataMapper.selectByExample(example);
        for (ProjectData projectData : projectDataList) {
            UrlDetailVo urlDetailVo = new UrlDetailVo();
            urlDetailVo.setUrl(projectData.getUrl());
            urlDetailVo.setName(projectData.getFileName());
            urlDetailVo.setUploadTime(projectData.getUploadTime());
            urlList.add(urlDetailVo);
        }
        return RespData.success(urlList);
    }

    /**
     * 获取报价单资料
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<UrlDetailVo>> getQuotationData(String projectNo) {
        List<UrlDetailVo> urlList = new ArrayList<>();
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andTypeEqualTo(ProjectDataStatus.QUOTATION_STATUS.getValue());
        List<ProjectData> projectDataList = projectDataMapper.selectByExample(example);
        for (ProjectData projectData : projectDataList) {
            UrlDetailVo urlDetailVo = new UrlDetailVo();
            urlDetailVo.setUrl(projectData.getUrl());
            urlDetailVo.setName(projectData.getFileName());
            urlDetailVo.setUploadTime(projectData.getUploadTime());
            urlList.add(urlDetailVo);
        }
        return RespData.success(urlList);
    }

    /**
     * 确认资料
     * @param projectNo
     * @param category
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> confirmVolumeRoomData(String projectNo, Integer category) {
        ProjectData projectData = new ProjectData();
        projectData.setIsConfirm(ProjectDataStatus.CONFIRM.getValue());
        projectData.setConfirmTime(new Date());
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andCategoryEqualTo(category);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        int i = projectDataMapper.updateByExampleSelective(projectData, example);
        if (i == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("确认失败!");
        }
        return RespData.success();
    }

    /**
     * 根据项目编号批量获取人员信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<UserVo>> getProjectUsers(String projectNo) {
        List<UserVo> userVoList = orderUserMapper.getProjectUsers(projectNo, UserStatus.NO_TRANSFER.getValue(), UserStatus.ON_JOB.getValue());
        return RespData.success(userVoList);
    }

    /**
     * 获取项目阶段
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<Integer> getProjectStatus(String projectNo) {
        List<OrderDetailsVO> orderDetailsVO = projectMapper.selectOrderDetails(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        return RespData.success(orderDetailsVO.get(0).getStage());
    }

    /**
     * 批量获取员工的信息
     *
     * @param userIds
     * @return
     */
    @Override
    public MyRespBundle<Map<String, UserVo>> getListUserByUserIds(List<String> userIds) {
        Map<String, UserVo> map = new HashMap<>();
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdIn(userIds);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        for (EmployeeMsg employeeMsg : employeeMsgs) {
            UserVo vo = new UserVo();
            vo.setRealName(employeeMsg.getRealName());
            vo.setRoleCode(employeeMsg.getRoleCode());
            vo.setUserId(employeeMsg.getUserId());
            vo.setRoleName(UserJobs.findByCodeStr(vo.getRoleCode()).mes);
            map.put(employeeMsg.getUserId(), vo);
        }
        return RespData.success(map);
    }

    /**
     * 退款
     *
     * @param orderNo
     * @param payOrderNo
     * @param otherReason
     * @param money
     * @param moneyName
     * @param userId
     * @param cancelReason
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> applyRefund(String orderNo, String payOrderNo, String otherReason, Integer money, String moneyName, String userId, String cancelReason) {
        OrderApplyRefund orderApplyRefund = new OrderApplyRefund();
        orderApplyRefund.setLaunchTime(new Date());
        orderApplyRefund.setType(ProjectDataStatus.REFUND_ONE.getValue());
        orderApplyRefund.setOrderNo(orderNo);
        orderApplyRefund.setPayOrderNo(payOrderNo);
        orderApplyRefund.setMoney(money);
        orderApplyRefund.setCancleReason(cancelReason);
        orderApplyRefund.setOtherReason(otherReason);
        orderApplyRefund.setMoneyName(moneyName);
        orderApplyRefund.setInitiatorId(userId);
        int i = orderApplyRefundMapper.insertSelective(orderApplyRefund);
        if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
            return RespData.error("退款申请失败!!");
        }
        return RespData.success();
    }
}
