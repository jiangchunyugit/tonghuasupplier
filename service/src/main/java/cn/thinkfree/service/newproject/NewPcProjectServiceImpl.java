package cn.thinkfree.service.newproject;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.appvo.OrderTaskSortVo;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.appvo.UserVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.pcvo.*;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.constants.UserStatus;
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

    /**
     * PC获取项目详情接口--项目阶段
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<OrderTaskSortVo>> getPcProjectTask(String projectNo) {
        //获取项目阶段信息,所有的阶段时间都以开始时间展示为主,展示所有的PC项目阶段
        List<OrderTaskSortVo> allOrderTask = new ArrayList<>();
        List<Map<String, Object>> designMaps = DesignStateEnum.allStates(ProjectDataStatus.PLAY_PLATFORM.getValue());
        List<Map<String, Object>> constructioMaps = ConstructionStateEnum.allStates(ProjectDataStatus.PLAY_PLATFORM.getValue());
        for (Map<String, Object> map : constructioMaps) {
            OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
            orderTaskSortVo.setSort((Integer) map.get("key"));
            orderTaskSortVo.setName(map.get("val").toString());
            allOrderTask.add(orderTaskSortVo);
        }
        for (Map<String, Object> map : designMaps) {
            OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
            orderTaskSortVo.setName(map.get("val").toString());
            orderTaskSortVo.setSort((Integer) map.get("key"));
            allOrderTask.add(orderTaskSortVo);
        }
        List<OrderTaskSortVo> orderTaskSortVoList = projectStageLogMapper.selectByProjectNo(projectNo);
        for (OrderTaskSortVo taskSortVo : orderTaskSortVoList) {
            for (OrderTaskSortVo taskSortVo1 : allOrderTask) {
                if (taskSortVo.getSort().equals(taskSortVo1.getSort())) {
                    taskSortVo1.setBeginTime(taskSortVo.getBeginTime());
                }
            }
        }
        return RespData.success(allOrderTask);
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
        PersionVo persionVo = employeeMsgMapper.selectByUserId(designerOrder.getUserId());
        if (designerOrder.getOrderStage().equals(DesignStateEnum.STATE_270.getState()) || designerOrder.getOrderStage().equals(DesignStateEnum.STATE_210.getState())) {
            designerOrderVo.setComplete(true);
        } else {
            designerOrderVo.setComplete(false);
        }
        if (persionVo != null) {
            designerOrderVo.setDesigner(persionVo.getName());
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
