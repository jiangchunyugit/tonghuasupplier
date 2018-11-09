package cn.thinkfree.database.pcvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel("合同信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractVo {
    @ApiModelProperty(value = "合同编号")
    private String contractNo;
    @ApiModelProperty(value = "签约时间")
    private Date contractTime;
    @ApiModelProperty(value = "合同状态")
    private String contractStatus;
//    @ApiModelProperty(value = "装修施工合同")
//    @ApiModelProperty(value = "个性化总表")
//    @ApiModelProperty(value = "客户主材明细")
}
