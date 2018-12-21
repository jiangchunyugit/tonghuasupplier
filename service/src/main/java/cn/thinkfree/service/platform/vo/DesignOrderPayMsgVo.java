package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xusonghui
 * 设计订单支付信息对象
 */
@Getter
@Setter
public class DesignOrderPayMsgVo {
    @ApiModelProperty("款项名称")
    private String typeSubName;
    @ApiModelProperty("优惠金额")
    private String discountMoney;
    @ApiModelProperty("应收金额")
    private String receivableMoney;
    @ApiModelProperty("已收金额")
    private String acceptedMoney;
    @ApiModelProperty("状态,1已支付，2未支付")
    private int state;
    @ApiModelProperty("支付时间")
    private String payTime;
    @ApiModelProperty("交易流水号")
    private String serialNumber;
}
