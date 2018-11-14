package cn.thinkfree.service.newproject;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.OrderTaskSortVo;
import cn.thinkfree.database.pcvo.*;

import java.util.List;

/**
 * PC项目相关
 *
 * @author gejiaming
 */
public interface NewPcProjectService {
    /**
     * 获取Pc端项目详情
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<List<OrderTaskSortVo>> getPcProjectTask(String projectNo);

    /**
     * PC获取项目详情接口--施工订单
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<ConstructionOrderVO> getPcProjectConstructionOrder(String projectNo);

    /**
     * PC获取项目详情接口--设计信息
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<DesignerOrderVo> getPcProjectDesigner(String projectNo);

    /**
     * PC获取项目详情接口--预交底信息
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<PreviewVo> getPcProjectPreview(String projectNo);

    /**
     * PC获取项目详情接口--报价信息
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<OfferVo> getPcProjectOffer(String projectNo);

    /**
     * PC获取项目详情接口--合同信息
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<ContractVo> getPcProjectContract(String projectNo);

    /**
     * PC获取项目详情接口--施工信息
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<SchedulingVo> getPcProjectScheduling(String projectNo);

    /**
     * PC获取项目详情接口--结算管理
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<SettlementVo> getPcProjectSettlement(String projectNo);

    /**
     * PC获取项目详情接口--评价管理
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<EvaluateVo> getPcProjectEvaluate(String projectNo);

    /**
     * PC获取项目详情接口--发票管理
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<InvoiceVo> getPcProjectInvoice(String projectNo);

    /**
     * 获取上海报价信息
     * @param designId
     * @return
     */
    MyRespBundle getShangHaiPriceDetail(String designId,String projectNo);


}
