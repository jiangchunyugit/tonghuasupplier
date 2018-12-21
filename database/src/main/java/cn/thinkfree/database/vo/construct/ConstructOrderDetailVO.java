package cn.thinkfree.database.vo.construct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 施工订单详情
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 11:23
 */
@ApiModel("施工订单详情")
@Data
public class ConstructOrderDetailVO {
    @ApiModelProperty("订单信息")
    private OrderDetailVO orderDetailVO;
    @ApiModelProperty("业主信息")
    private ConsumerDetailVO consumerDetailVO;
    @ApiModelProperty("施工信息")
    private ConstructDetailVO constructDetailVO;
    @ApiModelProperty("服务人员信息")
    private ServiceStaffsVO serviceStaffsVO;
    @ApiModelProperty("支付信息")
    private PayDetailVO payDetailVO;
    @ApiModelProperty("延期信息")
    private DelayDetailVO delayDetailVO;
}
