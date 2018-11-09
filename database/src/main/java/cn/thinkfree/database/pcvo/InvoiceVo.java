package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("发票管理")
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceVo {
    @ApiModelProperty(value = "发票类型")
    private String invoiceType;
    @ApiModelProperty(value = "发票内容")
    private String invoiceDetail;
    @ApiModelProperty(value = "发票抬头")
    private String invoiceTitle;
    @ApiModelProperty(value = "发票代码")
    private String invoiceNo;
    @ApiModelProperty(value = "已开发票")
    private boolean invoiced;
}
