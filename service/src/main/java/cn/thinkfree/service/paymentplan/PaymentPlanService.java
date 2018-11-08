package cn.thinkfree.service.paymentplan;

import cn.thinkfree.database.model.PaymentPlan;
import cn.thinkfree.database.vo.PaymentPlanVO;

import java.util.List;

public interface PaymentPlanService {

    /**
     * 支付列表
     * @return
     */
    List<PaymentPlan> paymentPlans();

    /**
     * 通过支付编码查找支付详情
     * @param paymentCode
     * @return
     */
    PaymentPlanVO paymentPlanDetails(String paymentCode);
}
