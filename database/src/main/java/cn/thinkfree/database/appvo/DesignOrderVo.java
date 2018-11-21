package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

@ApiModel("设计订单信息")
@Data
public class DesignOrderVo {
    @ApiModelProperty(value = "项目编号")
    private String projectNo;
    @ApiModelProperty(value = "项目状态")
    private String projectStage;
    @ApiModelProperty(value = "项目状态")
    private Integer stage;
    @ApiModelProperty(value = "设计订单编号")
    private String designerOrderNo;
    @ApiModelProperty(value = "项目创建时间")
    private Date projectCreateTime;




}
