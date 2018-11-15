package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.ConstructionStateVo;

/**
 *  施工状态
 */
public interface ConstructionStateServiceB {

    /**
     * 查询当前状态
     *
     * @param type  ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    MyRespBundle<String> getStateInfo(String orderNo,int type);

    /**
     * 运营平台
     * 派单给装饰公司
     */
     MyRespBundle<String> operateDispatchToConstruction(String orderNo);

    /**
     * 装饰公司
     * 1派单给服务人员 2施工报价完成 4合同录入 5确认线下签约完成（自动创建工地项目）
     */
     MyRespBundle<String> constructionState(String orderNo, int type);

    /**
     * 装饰公司
     *  3审核完成 (审核是否通过)
     */
    MyRespBundle<String> constructionStateOfExamine(String orderNo, int type,int isPass);

    /**
     * 支付
     * 1.首期款支付 2.开工报告 3.阶段验收通过 4.支付阶段款 5.等待尾款支付（验收通过）
     */
     MyRespBundle<String> customerPay(String orderNo, int type);

    /**
     * 订单完成
     * 支付尾款后
     */
     MyRespBundle<String> orderComplete(String orderNo);

    /**
     * 消费者
     * 取消订单
     * 签约阶段逆向
     */
     MyRespBundle<String> customerCancelOrder(String userId, String orderNo, String cancelReason);

    /**
     * 消费者
     * 取消订单
     * 支付未开工逆向
     */
     MyRespBundle<String> customerCancelOrderForPay(String orderNo);
}
