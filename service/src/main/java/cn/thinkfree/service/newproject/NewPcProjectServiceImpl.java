package cn.thinkfree.service.newproject;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.BasicsDataParentEnum;
import cn.thinkfree.core.constants.ConstructOrderConstants;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.model.OrderStatusDTO;
import cn.thinkfree.database.appvo.OrderTaskSortVo;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.appvo.UserVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.pcvo.*;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.constants.UserStatus;
import cn.thinkfree.service.construction.ConstructOrderService;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.contract.ContractService;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.neworder.ReviewDetailsService;
import cn.thinkfree.service.remote.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * PC项目相关
 *
 * @author gejiaming
 */
@Service
public class NewPcProjectServiceImpl implements NewPcProjectService {
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
    @Autowired
    CloudService cloudService;
    @Autowired
    ProjectQuotationMapper projectQuotationMapper;
    @Autowired
    ProjectQuotationRoomsMapper projectQuotationRoomsMapper;
    @Autowired
    ProjectQuotationRoomsConstructMapper roomsConstructMapper;
    @Autowired
    ProjectQuotationRoomsHardDecorationMapper hardDecorationMapper;
    @Autowired
    ProjectQuotationRoomsSoftDecorationMapper softDecorationMapper;
    @Autowired
    ProjectQuotationCheckMapper checkMapper;
    @Autowired
    ReviewDetailsService reviewDetailsService;
    @Autowired
    ProjectQuotationCheckMapper projectQuotationCheckMapper;
    @Autowired
    FundsOrderFeeMapper fundsOrderFeeMapper;
    @Autowired
    ContractService contractService;
    @Autowired
    BasicsDataMapper basicsDataMapper;
    @Autowired
    ConstructOrderService constructOrderService;
    @Autowired
    ConstructionStateService constructionStateService;

    /**
     * PC获取项目详情接口--项目阶段
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<OrderAllTaskVo> getPcProjectTask(String projectNo) {
        OrderAllTaskVo orderAllTaskVo = new OrderAllTaskVo();
        //获取项目阶段信息,所有的阶段时间都以开始时间展示为主,展示所有的PC项目阶段
        List<OrderTaskSortVo> allOrderTask = new ArrayList<>();
        ConstructionOrder constructionOrder = constructOrderService.findByProjectNo(projectNo);
        List<OrderStatusDTO> states = constructionStateService.getStates(ConstructOrderConstants.APP_TYPE_CUSTOMER, constructionOrder.getOrderStage());
        for (OrderStatusDTO orderStatus : states) {
            OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
            orderTaskSortVo.setSort(orderStatus.getStatus());
            orderTaskSortVo.setName(orderStatus.getName());
            allOrderTask.add(orderTaskSortVo);
        }
        orderAllTaskVo.setAllOrderTask(allOrderTask);
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() > 0) {
            orderAllTaskVo.setCurrentSort(projects.get(0).getStage());
        }
        return RespData.success(orderAllTaskVo);
    }

    /**
     * PC获取项目详情接口--施工订单
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ConstructionOrderVO> getPcProjectConstructionOrder(String projectNo) {
        //组合施工订单信息
        ConstructionOrderVO constructionOrderVO = constructionOrderMapper.selectConstructionOrderVo(projectNo);
        //添加业主信息
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return RespData.error("项目不存在!!");
        }
        Project project = projects.get(0);
        PersionVo owner = new PersionVo();
        try {
            Map userName1 = newOrderUserService.getUserName(project.getOwnerId(), ProjectDataStatus.OWNER.getDescription());
            owner.setPhone(userName1.get("phone").toString());
            owner.setName(userName1.get("nickName").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error("调取人员信息失败!");
        }
        constructionOrderVO.setOwnerName(owner.getName());
        constructionOrderVO.setOwnerPhone(owner.getPhone());
        BasicsDataExample basicsDataExample = new BasicsDataExample();
        BasicsDataExample.Criteria dataCriteria = basicsDataExample.createCriteria();
        dataCriteria.andBasicsCodeEqualTo(constructionOrderVO.getStyle());
        dataCriteria.andBasicsGroupEqualTo(BasicsDataParentEnum.DESIGN_STYLE.getCode());
        List<BasicsData> basicsData = basicsDataMapper.selectByExample(basicsDataExample);
        if (basicsData.size() == 0) {
            constructionOrderVO.setStyle("");
        } else {
            constructionOrderVO.setStyle(basicsData.get(0).getBasicsName());
        }
        //订单来源
        switch (project.getOrderSource()) {
            case 1:
                constructionOrderVO.setOrderSource("天猫");
                break;
            case 10:
                constructionOrderVO.setOrderSource("线下导入");
                break;
            case 20:
                constructionOrderVO.setOrderSource("运营平台创建");
                break;
            case 30:
                constructionOrderVO.setOrderSource("运营平台导入");
                break;
            case 40:
                constructionOrderVO.setOrderSource("设计公司创建");
                break;
            case 50:
                constructionOrderVO.setOrderSource("设计公司导入");
                break;
            default:
                constructionOrderVO.setOrderSource("其他");
                break;
        }
        //订单阶段
        constructionOrderVO.setOrderStage(ConstructionStateEnum.queryByState(project.getStage()).getStateName(ConstructOrderConstants.APP_TYPE_DESIGN));

        return RespData.success(constructionOrderVO);
    }

    /**
     * PC获取项目详情接口--设计信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<DesignerOrderVo> getPcProjectDesigner(String projectNo) {
        DesignerOrderVo designerOrderVo = new DesignerOrderVo();
        //组合设计信息
        DesignerOrderExample designerOrderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria designCriteria = designerOrderExample.createCriteria();
        designCriteria.andProjectNoEqualTo(projectNo);
        designCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(designerOrderExample);
        if (designerOrders.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.success(null);
        }
        DesignerOrder designerOrder = designerOrders.get(0);
        OrderUserExample userExample = new OrderUserExample();
        OrderUserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andRoleCodeEqualTo("CD");
        userCriteria.andProjectNoEqualTo(projectNo);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        if (orderUsers.size() > 0) {
            List<PersionVo> persionVos = employeeMsgMapper.selectByUserId(orderUsers.get(0).getUserId());
            if (persionVos.size() > 0 && persionVos.get(0) != null) {
                designerOrderVo.setDesigner(persionVos.get(0).getName());
            }
        }
        if (designerOrder.getOrderStage().equals(DesignStateEnum.STATE_270.getState()) || designerOrder.getOrderStage().equals(DesignStateEnum.STATE_210.getState())) {
            designerOrderVo.setComplete(true);
        } else {
            designerOrderVo.setComplete(false);
        }
        designerOrderVo.setProgrammeOneName("美丽家园");
        designerOrderVo.setProgrammeOneUrl("https://rs.homestyler.com/floorplan/render/images/2018-6-27/f5f15d55-f431-4e2b-9cef-1280e7e89d62/7ecbaa27_0545_4b66_8142_62a5454ecf54.jpg");
        return RespData.success(designerOrderVo);
    }

    /**
     * PC获取项目详情接口--预交底信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<PreviewVo> getPcProjectPreview(String projectNo) {
        //组合预交底信息
        PreviewVo previewVo = new PreviewVo(new Date(), "未审核", "张三", "李四", "王五");
        return RespData.success(previewVo);
    }

    /**
     * PC获取项目详情接口--报价信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<OfferVo> getPcProjectOffer(String projectNo) {
        //组合报价信息
        // OfferVo offerVo = new OfferVo("86", "1099", "20000", "1200", "2700", "2700", "2700", "通过", "刘万东", new Date());
        if (null == projectNo || "".equals(projectNo)) {
            return RespData.error("项目编号为空");
        }
        OfferVo offerVo = new OfferVo();
        ProjectQuotationExample projectQuotationExample = new ProjectQuotationExample();
        ProjectQuotationExample.Criteria criteria = projectQuotationExample.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<ProjectQuotation> projectQuotations = projectQuotationMapper.selectByExample(projectQuotationExample);
        if (projectQuotations.size() == 0) {
            return RespData.success(null);
        }
        if (projectQuotations.size() == 1) {
            ProjectQuotation projectQuotation = projectQuotations.get(0);
            //面积
            offerVo.setArea(projectQuotation.getInnerArea().toString());
            //平米单价
            offerVo.setUnitPrice(projectQuotation.getUnitPrice().toString());
            //总价
            offerVo.setTotalPrice(projectQuotation.getTotalPrice().toString());
            //基础施工保价
            offerVo.setBasePrice(projectQuotation.getConstructionTotalPrice().toString());
            //软装保价
            offerVo.setMainMaterialFee(projectQuotation.getSoftDecorationPrice().toString());
            //硬装保价
            offerVo.setOtherFee(projectQuotation.getHardDecorationPrice().toString());
            //变更保价
            FundsOrderFeeExample fundsOrderFeeExample = new FundsOrderFeeExample();
            FundsOrderFeeExample.Criteria criteria3 = fundsOrderFeeExample.createCriteria();
            criteria3.andProjectNoEqualTo(projectNo);
            List<FundsOrderFee> fundsOrderFees = fundsOrderFeeMapper.selectByExample(fundsOrderFeeExample);
            if (fundsOrderFees.size() == 1) {
                FundsOrderFee fundsOrderFee = fundsOrderFees.get(0);
                offerVo.setChangeFee(fundsOrderFee.getFeeAmount());
            }
            //
            ProjectQuotationCheckExample projectQuotationCheckExample = new ProjectQuotationCheckExample();
            ProjectQuotationCheckExample.Criteria criteria1 = projectQuotationCheckExample.createCriteria();
            criteria1.andProjectNoEqualTo(projectNo);
            criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
            List<ProjectQuotationCheck> projectQuotationChecks = projectQuotationCheckMapper.selectByExample(projectQuotationCheckExample);
            if (projectQuotationChecks.size() == 1) {
                ProjectQuotationCheck projectQuotationCheck = projectQuotationChecks.get(0);
                //审核状态
                offerVo.setAuditStatus(projectQuotationCheck.getCheckStatus().toString());
                //审核时间
                offerVo.setAuditTime(projectQuotationCheck.getApprovalTime());
                //审核人
                if (projectQuotationCheck.getApprovalId() != null) {
                    EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
                    EmployeeMsgExample.Criteria criteria2 = employeeMsgExample.createCriteria();
                    criteria2.andUserIdEqualTo(projectQuotationCheck.getApprovalId());
                    List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(employeeMsgExample);
                    if (employeeMsgs.size() == 1) {
                        EmployeeMsg employeeMsg = employeeMsgs.get(0);
                        offerVo.setPriceAuditor(employeeMsg.getRealName());
                    }
                }
            }

        }
        return RespData.success(offerVo);
    }

    /**
     * PC获取项目详情接口--合同信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ContractVo> getPcProjectContract(String projectNo) {
        if (projectNo == null || projectNo.trim().isEmpty()) {
            return RespData.error("前检查参数projectNo=" + projectNo);
        }
        //组合合同信息
        ConstructionOrderExample example = new ConstructionOrderExample();
        ConstructionOrderExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(example);
        if (constructionOrders.size() == 0) {
            return RespData.success(null);
        }
        ContractVo contractVo = contractService.getOrderContractByOrderNo(constructionOrders.get(0).getOrderNo());
        return RespData.success(contractVo);
    }

    /**
     * PC获取项目详情接口--施工信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<SchedulingVo> getPcProjectScheduling(String projectNo) {
        SchedulingVo schedulingVo = projectMapper.selectContractByProjectNo(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        if (schedulingVo == null) {
            return RespData.error("查无此施工信息!");
        }
        List<UserVo> userVoList = orderUserMapper.getProjectUsers(projectNo, UserStatus.NO_TRANSFER.getValue(), UserStatus.ON_JOB.getValue());
        if (userVoList.size() == 0) {
            return RespData.error("此项目尚无分配施工人员");
        }
        for (UserVo userVo : userVoList) {
            if (userVo.getRoleCode().equals(UserJobs.ProjectManager.roleCode)) {
                schedulingVo.setProjectManager(userVo.getRealName());
            }
            if (userVo.getRoleCode().equals(UserJobs.Foreman.roleCode)) {
                schedulingVo.setForeman(userVo.getRealName());
            }
            if (userVo.getRoleCode().equals(UserJobs.Steward.roleCode)) {
                schedulingVo.setHousekeeper(userVo.getRealName());
            }
            if (userVo.getRoleCode().equals(UserJobs.QualityInspector.roleCode)) {
                schedulingVo.setQualityInspector(userVo.getRealName());
            }
        }
        BasicsDataExample basicsDataExample = new BasicsDataExample();
        BasicsDataExample.Criteria dataCriteria = basicsDataExample.createCriteria();
        dataCriteria.andBasicsCodeEqualTo(schedulingVo.getStyle());
        dataCriteria.andBasicsGroupEqualTo(BasicsDataParentEnum.DESIGN_STYLE.getCode());
        List<BasicsData> basicsData = basicsDataMapper.selectByExample(basicsDataExample);
        if (basicsData.size() == 0) {
            schedulingVo.setStyle("");
        } else {
            schedulingVo.setStyle(basicsData.get(0).getBasicsName());
        }
        //验收结果
        ProjectQuotationCheckExample checkExample = new ProjectQuotationCheckExample();
        ProjectQuotationCheckExample.Criteria checkCriteria = checkExample.createCriteria();
        checkCriteria.andProjectNoEqualTo(projectNo);
        checkCriteria.andStatusEqualTo(1);
        List<ProjectQuotationCheck> projectQuotationChecks = projectQuotationCheckMapper.selectByExample(checkExample);
        if (projectQuotationChecks.size() > 0) {
            switch (projectQuotationChecks.get(0).getCheckStatus()) {
                case 1:
                    schedulingVo.setCheckResult("审核中");
                    break;
                case 2:
                    schedulingVo.setCheckResult("审核失败");
                    break;
                default:
                    schedulingVo.setCheckResult("审核通过");
                    break;
            }

        }


        return RespData.success(schedulingVo);
    }

    /**
     * PC获取项目详情接口--结算管理
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<SettlementVo> getPcProjectSettlement(String projectNo) {
        //组合结算信息
        //获取支付模块结算信息


        SettlementVo settlementVo = new SettlementVo("20000.00", "20000.00", "20000.00", "20000.00", "4000.00", "4000.00", "4000.00", "首付款", "0", "0");
        return RespData.success(settlementVo);
    }

    /**
     * PC获取项目详情接口--评价管理
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<EvaluateVo> getPcProjectEvaluate(String projectNo) {
        //组合评价管理
        EvaluateVo evaluateVo = new EvaluateVo(45, 36, 66);
        return RespData.success(evaluateVo);
    }

    /**
     * PC获取项目详情接口--发票管理
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<InvoiceVo> getPcProjectInvoice(String projectNo) {
        //组合发票管理
        InvoiceVo invoiceVo = new InvoiceVo("电子普通发票", "装修服务", "个人", "02956156154", true);
        return RespData.success(invoiceVo);
    }


}
