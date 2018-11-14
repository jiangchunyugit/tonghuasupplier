package cn.thinkfree.service.construction.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 精准报价列表
 */
@Getter
@Setter
@ApiModel("精准报价列表")
public class PrecisionPriceVo {


    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("项目编号")
    private String projectNo;

    @ApiModelProperty("申请时间")
    private Date appointmentTime;

    @ApiModelProperty("项目地址")
    private String addressDetail;

    @ApiModelProperty("面积")
    private Integer area;

    @ApiModelProperty("预算报价")
    private Integer decorationBudget;

    @ApiModelProperty("设计师")
    private String designerName;

    @ApiModelProperty("订单状态(审核状态)")
    private String orderStage;
}
