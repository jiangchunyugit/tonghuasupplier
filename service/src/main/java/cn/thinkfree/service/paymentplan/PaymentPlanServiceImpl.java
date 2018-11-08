package cn.thinkfree.service.paymentplan;

import cn.thinkfree.database.mapper.PaymentPlanMapper;
import cn.thinkfree.database.model.PaymentPlan;
import cn.thinkfree.database.model.PaymentPlanExample;
import cn.thinkfree.database.vo.PaymentPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentPlanServiceImpl implements PaymentPlanService {

    @Autowired
    PaymentPlanMapper paymentPlanMapper;

    @Override
    public List<PaymentPlan> paymentPlans() {

        PaymentPlanExample paymentPlanExample = new PaymentPlanExample();
        return paymentPlanMapper.selectByExample(paymentPlanExample);
    }

    @Override
    public PaymentPlanVO paymentPlanDetails(String paymentCode) {
        return paymentPlanMapper.paymentPlanDetails(paymentCode);
    }
}
