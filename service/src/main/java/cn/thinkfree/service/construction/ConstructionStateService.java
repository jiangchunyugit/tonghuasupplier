package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.model.OrderStatusDTO;

import java.util.List;
import java.util.Map;

/**
 * 施工状态
 */
public interface ConstructionStateService {

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
    MyRespBundle<Map<String, String>> getStateDetailInfo(String orderNo, int type);

    /**
     * 运营平台
     * 派单给装饰公司
     */
    MyRespBundle<String> operateDispatchToConstruction(String orderNo);

    /**
     * 装饰公司
     * 1派单给服务人员 2施工报价完成 4合同录入 5确认线下签约完成（自动创建工地项目）
     */
    void constructionState(String orderNo, int type);

    /**
     * 装饰公司
     * 3审核完成 (审核是否通过)
     */
    MyRespBundle<String> constructionStateOfExamine(String orderNo, int type, int isPass);

    /**
     * 装饰公司
     * 5确认线下签约完成（自动创建工地项目）
     */
    void contractCompleteState(String orderNo);

    /**
     * 首付款
     * @param orderNo
     * @return
     */
    MyRespBundle<Boolean> firstPay(String orderNo);

    /**
     * 支付
     */
    MyRespBundle<String> customerPay(String orderNo, String feeName, Integer sort,String isComplete);

    /**
     * 施工阶段方案
     */
    MyRespBundle<String> constructionPlan(String projectNo, Integer sort);

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
    MyRespBundle<String> customerCancelOrderForPay(String orderNo, int type);

    /**
     * 判断订单状态划分
     * @param state 订单状态
     * @param complaintState 订单状态 1,未投诉，2处理中，3关闭，4已取消
     * @param stateRange 订单状态分类 1,全部 2,待签约 3,待开工 4,施工中 5,已竣工
     * @return 订单状态划分
     */
    boolean getConstructState(int state, int complaintState, int stateRange);	/**
     * 根据类型获取订单状态类型列表
     * @param type 1：获取平台状态；2：获取装饰公司状态；3：获取施工人员状态；4：获取消费者状态
     * @param currentStatus 当前订单状态值
     * @return 订单状态信息
     */
    List<OrderStatusDTO> getStates(int type, Integer currentStatus);}
