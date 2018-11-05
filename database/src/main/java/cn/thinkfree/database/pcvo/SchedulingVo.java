package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel("施工信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingVo {
    @ApiModelProperty(value = "装修类型")
    private String style;
    @ApiModelProperty(value = "施工周期-开始时间")
    private Date startTime;
    @ApiModelProperty(value = "施工周期-结束时间")
    private Date endTime;
    @ApiModelProperty(value = "施工阶段")
    private String stage;
    @ApiModelProperty(value = "验收结果")
    private String checkResult;
    @ApiModelProperty(value = "合同金额")
    private String contractMoney;
    @ApiModelProperty(value = "项目经理")
    private String projectManager;
    @ApiModelProperty(value = "工长")
    private String foreman;
    @ApiModelProperty(value = "管家")
    private String housekeeper;
    @ApiModelProperty(value = "质检")
    private String qualityInspector;

}
