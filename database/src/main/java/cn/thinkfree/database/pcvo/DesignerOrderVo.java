package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@ApiModel("设计订单")
@AllArgsConstructor
@NoArgsConstructor
public class DesignerOrderVo {
    @ApiModelProperty(value = "设计是否完成")
    private Boolean complete;
    @ApiModelProperty(value = "设计师")
    private String designer;
    @ApiModelProperty(value = "图纸报价审核状态")
    private String auditStatus;
    @ApiModelProperty(value = "提交时间")
    private Date uploadTime;
    @ApiModelProperty(value = "方案1-name")
    private String programmeOneName;
    @ApiModelProperty(value = "方案1-url")
    private String programmeOneUrl;
    @ApiModelProperty(value = "方案2-name")
    private String programmeTwoName;
    @ApiModelProperty(value = "方案2-url")
    private String programmeTwoUrl;
    @ApiModelProperty(value = "方案3-name")
    private String programmeThreeName;
    @ApiModelProperty(value = "方案3-url")
    private String programmeThreeUrl;
}
