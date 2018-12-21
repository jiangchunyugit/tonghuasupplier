package cn.thinkfree.database.vo.construct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付信息
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 11:42
 */
@ApiModel("支付信息")
@Data
public class PayDetailVO {
    @ApiModelProperty("合同金额")
    private String compactMoney;
    @ApiModelProperty("已付款")
    private String payedMoney;
    @ApiModelProperty("待付款")
    private String waitPayMoney;
}
