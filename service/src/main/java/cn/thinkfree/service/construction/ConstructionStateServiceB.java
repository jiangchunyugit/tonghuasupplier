package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.ConstructionStateVo;

import java.util.Map;

/**
 * 施工状态
 */
public interface ConstructionStateServiceB {

    /**
     * 查询当前状态
     *
     * @param type ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    MyRespBundle<String> getStateInfo(String orderNo, int type);

    /**
     * 查询当前状态/付款施工详细阶段
     *
     * @param type ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    MyRespBundle<Map<String,String>> getStateDetailInfo(String orderNo, int type);

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
     * 3审核完成 (审核是否通过)
     */
    MyRespBundle<String> constructionStateOfExamine(String orderNo, int type, int isPass);


    /**
     * 装饰公司
     * 4合同录入 （完成）
     */
    void contractState (String orderNo);

    /**
     * 装饰公司
     * 5确认线下签约完成（自动创建工地项目）
     */
    void contractCompleteState(String orderNo);

    /**
     * 支付
     */
    MyRespBundle<String> customerPay(String orderNo, String feeName, String sort, String isEnd);

    /**
     * 施工阶段方案
     */
    MyRespBundle<String> constructionPlan(String projectNo, String sort, String isEnd);

    /**
     * 消费者
     * 取消订单
     * 签约阶段逆向
     */
    MyRespBundle<String> customerCancelOrder(String userId, String orderNo, String cancelReason);


    /**
     * 消费者
     * 取消订单
     * 签约阶段逆向
     * 查看状态
     */
    Boolean customerCancelOrderState(String userId, String orderNo);

    /**
     * 消费者
     * 取消订单
     * 支付未开工逆向
     */
    MyRespBundle<String> customerCancelOrderForPay(String orderNo,int type);
}
