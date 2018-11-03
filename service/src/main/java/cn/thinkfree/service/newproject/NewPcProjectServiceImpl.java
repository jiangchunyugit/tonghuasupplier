package cn.thinkfree.service.newproject;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.appvo.OrderTaskSortVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.OrderUser;
import cn.thinkfree.database.model.OrderUserExample;
import cn.thinkfree.database.pcvo.*;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.UserRoleStatus;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * PC项目相关
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


    /**
     * PC获取项目详情接口--项目阶段
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<OrderTaskSortVo>> getPcProjectTask(String projectNo) {
        //获取项目阶段信息
        List<OrderTaskSortVo> orderTaskSortVoList = projectStageLogMapper.selectByProjectNo(projectNo);
        for (OrderTaskSortVo taskSortVo:orderTaskSortVoList){
            try {
                if(!DesignStateEnum.queryByState(taskSortVo.getSort()).getStateName(ProjectDataStatus.PLAY_PLATFORM.getValue()).isEmpty()){
                    taskSortVo.setName(DesignStateEnum.queryByState(ProjectDataStatus.PLAY_PLATFORM.getValue()).getStateName(ProjectDataStatus.PLAY_PLATFORM.getValue()));
                }
                if(ConstructionStateEnum.queryByState(taskSortVo.getSort()).getStateName(ProjectDataStatus.PLAY_PLATFORM.getValue()).isEmpty()){
                    taskSortVo.setName(ConstructionStateEnum.queryByState(taskSortVo.getSort()).getStateName(ProjectDataStatus.PLAY_PLATFORM.getValue()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return RespData.success(orderTaskSortVoList);
    }

    /**
     * PC获取项目详情接口--施工订单
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
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<DesignerOrderVo> getPcProjectDesigner(String projectNo) {
        //假信息
        DesignerOrderVo designerOrderVo = new DesignerOrderVo(true,"刘强东","审核中",new Date(),"方案1","https://www.baidu.com/","方案2","https://www.baidu.com/","方案3","https://www.baidu.com/");
        return RespData.success(designerOrderVo);
    }

    /**
     * PC获取项目详情接口--预交底信息
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<PreviewVo> getPcProjectPreview(String projectNo) {
        //组合预交底信息
        PreviewVo previewVo = new PreviewVo(new Date(),"未审核","张三","李四","王五");
        return RespData.success(previewVo);
    }

    /**
     * PC获取项目详情接口--报价信息
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<OfferVo> getPcProjectOffer(String projectNo) {
        //组合报价信息
        OfferVo offerVo = new OfferVo("86","1099","20000","1200","2700","2700","2700","通过","刘万东",new Date());
        return RespData.success(offerVo);
    }

    /**
     * PC获取项目详情接口--合同信息
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ContractVo> getPcProjectContract(String projectNo) {
        //组合合同信息
        ContractVo contractVo = new ContractVo("HGSD6893",new Date(),"生效");
        return RespData.success(contractVo);
    }

    /**
     * PC获取项目详情接口--施工信息
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<SchedulingVo> getPcProjectScheduling(String projectNo) {
        //组合施工信息
        SchedulingVo schedulingVo = new SchedulingVo("个性化",new Date(),new Date(),"水电工程","隐蔽验收完成","20000.0","刘欢","张富贵","刘勋","张恒");
        return RespData.success(schedulingVo);
    }

    /**
     * PC获取项目详情接口--结算管理
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<SettlementVo> getPcProjectSettlement(String projectNo) {
        //组合结算信息
        SettlementVo settlementVo = new SettlementVo("20000.00","20000.00","20000.00","20000.00","4000.00","4000.00","4000.00","首付款","0","0");
        return RespData.success(settlementVo);
    }

    /**
     * PC获取项目详情接口--支付信息
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<PaymentVo> getPcProjectPayment(String projectNo) {
        //组合支付信息
        PaymentVo paymentVo;
        return null;
    }

    /**
     * PC获取项目详情接口--评价管理
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<EvaluateVo> getPcProjectEvaluate(String projectNo) {
        //组合评价管理
        EvaluateVo evaluateVo = new EvaluateVo(45,36,66);
        return RespData.success(evaluateVo);
    }

    /**
     * PC获取项目详情接口--发票管理
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<InvoiceVo> getPcProjectInvoice(String projectNo) {
        //组合发票管理
        InvoiceVo invoiceVo = new InvoiceVo("电子普通发票","装修服务","个人","02956156154",true);
        return RespData.success(invoiceVo);
    }
}
