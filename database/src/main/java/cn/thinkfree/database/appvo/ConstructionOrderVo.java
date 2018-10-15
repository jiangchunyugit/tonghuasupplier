package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
/**
 * @author gejiaming
 */
@ApiModel(value = "ConstructionOrderVo",description = "施工订单详情")
@Data
public class ConstructionOrderVo {
    @ApiModelProperty(name = "projectBigSchedulingVOList",value = "施工任务")
    private List<SchedulingBaseBigVo> projectBigSchedulingList;
    @ApiModelProperty(name = "taskStage",value = "任务阶段")
    private Integer taskStage;
    @ApiModelProperty(name = "orderNo",value = "订单编号")
    private String orderNo;
    @ApiModelProperty(name = "type",value = "装修类型")
    private Integer type;
    @ApiModelProperty(name = "cancle",value = "能否取消")
    private Boolean cancle;
    @ApiModelProperty(name = "constructionOrderPlayVo",value = "承接信息")
    private ConstructionOrderPlayVo constructionOrderPlayVo;

}
