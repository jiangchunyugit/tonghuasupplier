package cn.thinkfree.database.appVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "DesignOrderVo",description = "设计订单详情")
@Data
public class DesignOrderVo {
    @ApiModelProperty(name = "orderNo",value = "订单编号")
    private String orderNo;
    @ApiModelProperty(name = "type",value = "装修类型")
    private Integer type;
}
