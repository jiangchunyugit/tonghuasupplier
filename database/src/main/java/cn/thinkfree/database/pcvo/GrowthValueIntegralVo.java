package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("成长值与积分基础实体")
public class GrowthValueIntegralVo {
    @ApiModelProperty(value = "序号")
    private Integer sort;
    @ApiModelProperty(value = "任务编号")
    private String taskNo;
    @ApiModelProperty(value = "任务内容")
    private String taskDetail;
    @ApiModelProperty(value = "成长值奖励")
    private Integer growthValueAward;
    @ApiModelProperty(value = "积分奖励")
    private Integer integralReward;
}
