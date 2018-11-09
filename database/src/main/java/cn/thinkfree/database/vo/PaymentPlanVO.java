package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PaymentPlan;
import cn.thinkfree.database.model.PaymentProgram;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu 支付方案信息
 */
@ApiModel("支付方案详情")
public class PaymentPlanVO extends PaymentPlan{

    @ApiModelProperty("支付方案详细")
    private List<PaymentProgram> paymentPrograms;

    public List<PaymentProgram> getPaymentPrograms() {
        return paymentPrograms;
    }

    public void setPaymentPrograms(List<PaymentProgram> paymentPrograms) {
        this.paymentPrograms = paymentPrograms;
    }
}
