package cn.thinkfree.service.construction.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xusonghui
 * 施工订单列表
 */
@Getter
@Setter
public class ConsListVo {
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("施工订单编号")
    private String orderNo;
    @ApiModelProperty("省份名称")
    private String provinceName;
    @ApiModelProperty("城市名称")
    private String cityName;
    @ApiModelProperty("地区名称")
    private String areaName;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("创建时间，yyyy-MM-dd")
    private String createTime;
    @ApiModelProperty("签约时间，yyyy-MM-dd")
    private String signTime;
    @ApiModelProperty("项目地址")
    private String address;
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String ownerPhone;
    @ApiModelProperty("业主ID")
    private String ownerId;
    @ApiModelProperty("当前状态名称")
    private String stateName;
    @ApiModelProperty("项目经理名称")
    private String cpName;
    @ApiModelProperty("设计师姓名")
    private String cdName;
    @ApiModelProperty("工长姓名")
    private String cmName;
    @ApiModelProperty("管家姓名")
    private String csName;
    @ApiModelProperty("1是待运营平台派单，2待公司派单，3不展示派单按钮")
    private int isDistribution;
}
