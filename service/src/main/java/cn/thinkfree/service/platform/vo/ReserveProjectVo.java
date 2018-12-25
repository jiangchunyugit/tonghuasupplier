package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author xusonghui
 */
@Getter
@Setter
public class ReserveProjectVo {
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String phone;
    @ApiModelProperty("装修地址")
    private String address;
    @ApiModelProperty("来源（1,运营后台创建，2设计公司创建，3天猫，4业主预约）")
    private Integer source;
    @ApiModelProperty("装修风格")
    private Integer style;
    @ApiModelProperty("装修预算")
    private String budget;
    @ApiModelProperty("房屋面积")
    private String acreage;
    @ApiModelProperty("转换状态，1待转换，2已转换，3业主取消，4其他")
    private Integer state;
    @ApiModelProperty("所属公司ID")
    private String companyId;
    @ApiModelProperty("创建者userId")
    private String createUserId;
    @ApiModelProperty("取消原因")
    private String reason;
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("设计订单编号")
    private String designOrderNo;
    @ApiModelProperty("预约单编号")
    private String reserveNo;
    @ApiModelProperty("预约时间")
    private Date reserveTime;
    @ApiModelProperty("订单转换时间")
    private Date changeTime;
    @ApiModelProperty("指定设计师ID")
    private String designerId;
    @ApiModelProperty("省份编码")
    private String province;
    @ApiModelProperty("城市编码")
    private String city;
    @ApiModelProperty("区编码")
    private String area;
    @ApiModelProperty("省份")
    private String provinceName;
    @ApiModelProperty("城市")
    private String cityName;
    @ApiModelProperty("区")
    private String areaName;
    @ApiModelProperty("新旧程度")
    private String oldOrNew;
    @ApiModelProperty("户型")
    private String huxing;
}
