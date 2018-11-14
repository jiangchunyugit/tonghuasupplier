package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.util.Date;

@ApiModel("精准报价审核VO")
@Data
public class ProjectQuotationCheckVo {
    @ApiModelProperty(value = "提交时间")
    private Date submitTime;
    @ApiModelProperty(value = "报价审核状态(1,审核中 2,审核失败 3,审核通过)")
    private Integer checkStatus;
    @ApiModelProperty(value = "审核结果(1,通过 2,不通过)")
    private Integer result;
    @ApiModelProperty(value = "不通过原因")
    private String refuseReason;
    @ApiModelProperty(value = "审核次数")
    private Integer checkNum;
    @ApiModelProperty(value = "项目编号")
    private String projectNo;
}
