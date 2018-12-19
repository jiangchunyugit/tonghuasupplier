package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("订单信息")
@Data
public class ProjectDetailVO {
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String ownerPhone;
    @ApiModelProperty("装修地址")
    private String adress;
    @ApiModelProperty("详细地址")
    private String adressDetails;
    @ApiModelProperty("房屋类型")
    private String propertyType;
    @ApiModelProperty("建筑面积")
    private Integer area;
    @ApiModelProperty("常住人口")
    private Integer permanentResidents;
    @ApiModelProperty("户型")
    private String houseType;
    @ApiModelProperty("装修预算")
    private Integer decorationBudget;
    @ApiModelProperty("装修风格")
    private String style;
    @ApiModelProperty("计划装修开始时间")
    private String planStartTime;
    @ApiModelProperty("计划装修结束时间")
    private String planEndTime;
    @ApiModelProperty("订单来源")
    private String orderSource;

    @ApiModelProperty("订单编号")
    private String designOrderNo;
    @ApiModelProperty("承揽公司")
    private String companyName;
    @ApiModelProperty("设计师")
    private String designerName;

}
