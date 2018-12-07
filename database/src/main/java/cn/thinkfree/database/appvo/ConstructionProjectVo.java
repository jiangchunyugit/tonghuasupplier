package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel(value = "施工端项目列表实体")
@Data
public class ConstructionProjectVo {
    @ApiModelProperty(value = "项目编号")
    private String projectNo;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "预约时间  订单创建时间")
    private String appointmentTime;
    @ApiModelProperty(value = "装修类型")
    private String type;
    @ApiModelProperty(value = "项目阶段")
    private String stageName;
    @ApiModelProperty(name = "address", value = "装修地址")
    private String address;
    @ApiModelProperty(value = "3d图片地址")
    private String thirdUrl;
    @ApiModelProperty("业主姓名")
    private String owner;
    @ApiModelProperty("业主电话")
    private String phone;
    private Integer stage;
    private Integer delay;
    private Integer sort;

}
