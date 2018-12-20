package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 订单详情对象
 */
public class PcOrderMsgVo {
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("创建时间")
    private long createTime;
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主ID")
    private String ownerId;
    @ApiModelProperty("业主手机号")
    private String orderPhone;
    @ApiModelProperty("装修地址")
    private String address;
    @ApiModelProperty("详细地址")
    private String addressDetails;
    @ApiModelProperty("房屋类型，新房/旧房")
    private String houseType;
    @ApiModelProperty("建筑面积")
    private String area;
    @ApiModelProperty("户型")
    private String huxing;
    @ApiModelProperty("装修预算")
    private String budget;
    @ApiModelProperty("装修风格")
    private String style;
    @ApiModelProperty("常驻人口")
    private int peopleNo;
    @ApiModelProperty("计划装修开始时间")
    private long planStartTime;
    @ApiModelProperty("计划装修结束时间")
    private long planEndTime;
    @ApiModelProperty("归属设计公司ID")
    private String companyId;
    @ApiModelProperty("归属设计公司名称")
    private String companyName;
    @ApiModelProperty("归属设计师ID")
    private String designerId;
    @ApiModelProperty("归属设计师名称")
    private String designerName;
    @ApiModelProperty("订单来源")
    private String orderSource;
    @ApiModelProperty("订单来源名称")
    private String orderSourceName;

}
