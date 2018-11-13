package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: jiang
 * @Date: 2018/11/13 10:49
 * @Description: 硬装保价详情
 */
@ApiModel("硬装保价详情")
@Getter
@Setter
public class HardQuoteVO {
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
    private Integer unitPrice;
    @ApiModelProperty("数量")
    private Integer usedQuantity;
    @ApiModelProperty("总价")
    private Integer totalPrice;
}
