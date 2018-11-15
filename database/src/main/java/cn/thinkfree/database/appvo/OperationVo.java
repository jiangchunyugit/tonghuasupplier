package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationVo {
    @ApiModelProperty(value = "进度验收（验收")
    private String check;
    @ApiModelProperty(value = "材料进场（主材）")
    private String material;
    @ApiModelProperty(value = "问题整改（整改")
    private String problem;
    @ApiModelProperty(value = "延期确认（延期）")
    private String delay;
    @ApiModelProperty(value = "施工变更（变更）")
    private String change;
    @ApiModelProperty(value = "设计交付")
    private String designDeliver;
    @ApiModelProperty(value = ";计划排期")
    private String planScheduling;
    @ApiModelProperty(value = ";项目资料")
    private String projectData;
}
