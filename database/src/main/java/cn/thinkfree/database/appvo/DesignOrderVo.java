package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author gejiaming
 */
@ApiModel(value = "DesignOrderVo",description = "设计订单详情")
@Data
public class DesignOrderVo {
    @ApiModelProperty(name = "designTask",value = "装修任务")
    private List<DesignBaseVo> designTask;
    @ApiModelProperty(name = "taskStage",value = "任务阶段")
    private Integer taskStage;
    @ApiModelProperty(name = "orderNo",value = "订单编号")
    private String orderNo;
    @ApiModelProperty(name = "type",value = "装修类型")
    private Integer type;
    @ApiModelProperty(name = "designOrderPlayVo",value = "承接信息")
    private DesignOrderPlayVo designOrderPlayVo;
    @ApiModelProperty(name = "cancle",value = "能否取消")
    private Boolean cancle;
}
