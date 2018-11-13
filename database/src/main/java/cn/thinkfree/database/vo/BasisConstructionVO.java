package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Auther: jiang
 * @Date: 2018/11/13 10:37
 * @Description: 基础施工详情
 */
@ApiModel("基础施工详情")
@Getter
@Setter
public class BasisConstructionVO {
    @ApiModelProperty("施工项目编号")
    private String projectNo;
    @ApiModelProperty("项目名称")
    private String constructCode;
    @ApiModelProperty("项目名称")
    private String roomType;
    @ApiModelProperty("项目说明")
    private String constructName;
    @ApiModelProperty("单价")
    private Integer unitPrice;
    @ApiModelProperty("数量")
    private Integer usedQuantity;
    @ApiModelProperty("总价")
    private Integer totalPrice;
    @ApiModelProperty(value = "0,否 1,是")
    private Integer isDelete = 0;
    @ApiModelProperty(value = "0,否 1,是")
    private Integer isAdd = 0;
    @ApiModelProperty(value = "0,否 1,是")
    private Integer isEdit = 0;
}
