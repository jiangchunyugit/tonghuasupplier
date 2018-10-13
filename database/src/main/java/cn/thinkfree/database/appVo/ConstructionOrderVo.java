package cn.thinkfree.database.appVo;

import cn.thinkfree.database.model.ProjectBigScheduling;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
    @ApiModelProperty(name = "constructionCompany",value = "承接公司")
    private String constructionCompany;
    @ApiModelProperty(name = "foreman",value = "工长")
    private String foreman;
    @ApiModelProperty(name = "phone",value = "电话")
    private String phone;
    @ApiModelProperty(name = "taskNum",value = "任务数")
    private Integer taskNum;
    @ApiModelProperty(name = "cost",value = "费用")
    private Integer cost;
    @ApiModelProperty(name = "schedule",value = "工期")
    private Integer schedule;
    @ApiModelProperty(name = "delay",value = "延迟天数")
    private Integer delay;
    @ApiModelProperty(name = "cancle",value = "能否取消")
    private Boolean cancle;
//    @ApiModelProperty(name = "",value = "")

}
