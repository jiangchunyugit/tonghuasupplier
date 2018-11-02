package cn.thinkfree.service.construction.vo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 施工订单管理列表-反参实体
 * 运营后台
 */
@Getter
@Setter
@Api(tags = "施工订单管理列表-反参实体（运营后台）")
public class ConstructionOrderListVo {

    @ApiModelProperty("所属地区1")
    private String address;

    @ApiModelProperty("公司名称2")
    private String companyName;

    @ApiModelProperty("订单编号3")
    private String orderNo;

    @ApiModelProperty("项目编号4")
    private String projectNo;

    @ApiModelProperty("预约日期5")
    private Date appointmentTime;

    @ApiModelProperty("签约日期6")
    private Date signedTime;

    @ApiModelProperty("项目地址7")
    private String addressDetail;

    @ApiModelProperty("业主8")
    private String owner;

    @ApiModelProperty("手机号码9")
    private String phone;

    @ApiModelProperty("折后合同额10")
    private Integer reducedContractAmount;

    @ApiModelProperty("已支付11")
    private Integer havePaid;

    @ApiModelProperty("订单状态12")
    private Integer orderStage;

    @ApiModelProperty("施工进度13")
    private String constructionProgress;

    @ApiModelProperty("最近验收情况14")
    private Integer checkCondition;

    @ApiModelProperty("延期天数15")
    private Integer delayDays;

    @ApiModelProperty("项目经理16")
    private String projectManager;

    @ApiModelProperty("设计师17")
    private String designerName;


}
