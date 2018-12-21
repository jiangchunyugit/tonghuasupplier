package cn.thinkfree.database.vo.construct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单详情
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 11:25
 */
@ApiModel("订单详情")
@Data
public class OrderDetailVO {
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("订单类型")
    private String orderType;
}
