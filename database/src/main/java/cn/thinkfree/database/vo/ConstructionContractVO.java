package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Auther: jiang
 * @Date: 2018/11/11 17:37
 * @Description:
 */
@Getter
@Setter
@ApiModel("施工合同列表")
public class ConstructionContractVO {
    @ApiModelProperty("公司编码")
    private String companyId;
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("序号")
    private Integer sort;
    @ApiModelProperty("合同编号(模糊条件)")
    private String contractNo;
    @ApiModelProperty("订单编号(模糊条件)")
    private String orderNo;
    @ApiModelProperty("子订单编号")
    private String sunOrderNo;
    @ApiModelProperty("签约时间")
    private Date signingTime;
    @ApiModelProperty("签约时间区间开始(模糊条件)")
    private String startSign;
    @ApiModelProperty("签约时间区间结束(模糊条件)")
    private String endSign;
    @ApiModelProperty("订单来源(模糊条件)(1,天猫 2,线下)")
    private Integer orderSource;
    @ApiModelProperty("订单所在地(模糊条件)")
    private String orderAddress;
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String ownerPhone;
    @ApiModelProperty("合同金额")
    private Integer contractAmount;
    @ApiModelProperty("合同状态 0不生效 1生效")
    private Integer contractStatus;
    @ApiModelProperty("审批状态：0：不通过 1：通过")
    private Integer auditType;
    @ApiModelProperty("合同状态(模糊条件)：0：不通过 1：通过")
    private String flag;
}
