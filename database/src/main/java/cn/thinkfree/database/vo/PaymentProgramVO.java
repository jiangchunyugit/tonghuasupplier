package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PaymentProgram;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jiangchunyu 支付方案
 */
@ApiModel("支付方案信息")
public class PaymentProgramVO{

    @ApiModelProperty("方案编码")
    @NotBlank(message = "方案编号不可为空",groups = {Severitys.Insert.class,Severitys.Update.class})
    private String paymentProgramCode;

    @ApiModelProperty("方案名称")
    @NotBlank(message = "方案名称不可为空",groups = {Severitys.Insert.class})
    private String paymentProgramNm;

    @ApiModelProperty("支付方案集合")
    private List<PaymentProgram> paymentProgramList;

    public List<PaymentProgram> getPaymentProgramList() {
        return paymentProgramList;
    }

    public void setPaymentProgramList(List<PaymentProgram> paymentProgramList) {
        this.paymentProgramList = paymentProgramList;
    }

    public String getPaymentProgramCode() {
        return paymentProgramCode;
    }

    public void setPaymentProgramCode(String paymentProgramCode) {
        this.paymentProgramCode = paymentProgramCode;
    }

    public String getPaymentProgramNm() {
        return paymentProgramNm;
    }

    public void setPaymentProgramNm(String paymentProgramNm) {
        this.paymentProgramNm = paymentProgramNm;
    }
}
