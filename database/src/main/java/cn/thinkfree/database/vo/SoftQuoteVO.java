package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Auther: jiang
 * @Date: 2018/11/13 10:59
 * @Description: 软装保价详情
 */
@ApiModel("软装保价详情")
@Getter
@Setter
public class SoftQuoteVO {
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("房间类型")
    private String roomType;
    @ApiModelProperty("产品名称")
    private String materialName;
    @ApiModelProperty("品牌")
    private String brand;
    @ApiModelProperty("型号")
    private String model;
    @ApiModelProperty("规格")
    private String spec;
    @ApiModelProperty("单价")
    private BigDecimal unitPrice;
    @ApiModelProperty("数量")
    private Integer usedQuantity;
    @ApiModelProperty("总价")
    private BigDecimal totalPrice;
    @ApiModelProperty("数据唯一ID")
    private String id;
}
