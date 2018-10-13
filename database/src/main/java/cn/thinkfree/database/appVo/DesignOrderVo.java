package cn.thinkfree.database.appVo;

import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;
import java.util.Map;

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
    @ApiModelProperty(name = "designCompany",value = "承接公司")
    private String designCompany;
    @ApiModelProperty(name = "designer",value = "设计师")
    private String designer;
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
}
