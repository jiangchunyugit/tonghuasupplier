package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel("报价信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferVo {
    @ApiModelProperty(value = "面积/平米")
    private String area;
    @ApiModelProperty(value = "平米单价/元")
    private String unitPrice;
    @ApiModelProperty(value = "总价/元")
    private String totalPrice;
    @ApiModelProperty(value = "基础施工费用/元")
    private String basePrice;
    @ApiModelProperty(value = "主材费用/元")
    private String mainMaterialFee;
    @ApiModelProperty(value = "其他费用/元")
    private String otherFee;
    @ApiModelProperty(value = "变更报价/元")
    private String changeFee;
    @ApiModelProperty(value = "审核状态")
    private String auditStatus;
    @ApiModelProperty(value = "审价员")
    private String priceAuditor;
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;
}
