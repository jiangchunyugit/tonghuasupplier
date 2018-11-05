package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("施工订单")
@Data
public class ConstructionOrderVO {
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "关闭时间")
    private Date closeTime;
    @ApiModelProperty(value = "订单状态")
    private Integer orderStage;
    @ApiModelProperty(value = "订单来源")
    private String orderSource;
    @ApiModelProperty(value = "用户id")
    private String ownerId;
    @ApiModelProperty(value = "业主")
    private String ownerName;
    @ApiModelProperty(value = "项目地址")
    private String addressDetail;
    @ApiModelProperty(value = "房屋类型")
    private Integer houseType;
    @ApiModelProperty(value = "装修预算")
    private Integer decorationBudget;
    @ApiModelProperty(value = "卧室数量")
    private Integer houseRoom;
    @ApiModelProperty(value = "客厅数量")
    private Integer houseOffice;
    @ApiModelProperty(value = "卫生间数量")
    private Integer houseToilet;
    @ApiModelProperty(value = "面积")
    private Integer area;
    @ApiModelProperty(value = "装修风格")
    private String style;
    @ApiModelProperty(value = "参考方案")
    private String referenceScheme;
    @ApiModelProperty(value = "备注")
    private String remark;

}
