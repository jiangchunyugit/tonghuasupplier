package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

@ApiModel("结算信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementVo {
    @ApiModelProperty(value = "合同金额")
    private String contractMoney;
    @ApiModelProperty(value = "优惠金额")
    private String discountMoney;
    @ApiModelProperty(value = "折后合同金额")
    private String amountAfterDiscountMoney;
    @ApiModelProperty(value = "变更金额")
    private String changeMoney;
    @ApiModelProperty(value = "应收金额")
    private String amountReceivable;
    @ApiModelProperty(value = "已收金额")
    private String amountReceived;
    @ApiModelProperty(value = "待收金额")
    private String waitMoney;
    @ApiModelProperty(value = "付款阶段")
    private String payStage;
    @ApiModelProperty(value = "已发起金额")
    private String initiatedAmount;
//    @ApiModelProperty(value = "已收金额")
    @ApiModelProperty(value = "当前待收金额")
    private String currentAmountToBeReceived;
}
