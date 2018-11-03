package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gejiaming
 */
@ApiModel(value = "ProjectOrderDetailVo",description = "订单详情")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOrderDetailVo {
    @ApiModelProperty(name = "orderTaskSortVoList",value = "订单任务")
    private List<OrderTaskSortVo> orderTaskSortVoList;
    @ApiModelProperty(name = "taskStage",value = "任务阶段")
    private Integer taskStage;
    @ApiModelProperty(name = "playTask",value = "展示内容")
    private String playTask;
    @ApiModelProperty(name = "playTaskColor",value = "展示内容颜色")
    private String playTaskColor;
    @ApiModelProperty(name = "orderNo",value = "订单编号")
    private String orderNo;
    @ApiModelProperty(name = "orderType",value = "订单类型,1 设计订单, 2 施工订单")
    private Integer orderType;
    @ApiModelProperty(name = "type",value = "装修类型")
    private String styleType;
    @ApiModelProperty(name = "cancle",value = "能否取消")
    private Boolean cancle;
    @ApiModelProperty(name = "orderPlayVo",value = "展示信息")
    private OrderPlayVo orderPlayVo;
//    private List<FlexibleOrderPlayVo> flexibleOrderPlayVos;

}
