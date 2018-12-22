package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@ApiModel("设计订单信息")
@Data
@Setter
@Getter
public class DesignOrderVo {
    @ApiModelProperty(value = "项目编号")
    private String projectNo;
    @ApiModelProperty(value = "项目状态")
    private String projectStage;
    @ApiModelProperty(value = "项目状态")
    private Integer stage;
    @ApiModelProperty(value = "设计订单编号")
    private String designerOrderNo;
    @ApiModelProperty(value = "项目创建时间")
    private Date projectCreateTime;
    @ApiModelProperty(value = "业主姓名")
    private String ownerName;
    @ApiModelProperty(value = "业主手机号")
    private String ownerPhone;
    @ApiModelProperty(value = "业主ID")
    private String ownerId;
    @ApiModelProperty("项目地址")
    private String address;
    @ApiModelProperty("所在省份")
    private String provinceName;
    @ApiModelProperty("所在市")
    private String cityName;
    @ApiModelProperty("所在区域")
    private String areaName;
    @ApiModelProperty("所在省份编码")
    private String province;
    @ApiModelProperty("所在市编码")
    private String city;
    @ApiModelProperty("所在区域编码")
    private String region;
    @ApiModelProperty("所属公司名称")
    private String companyName;
    @ApiModelProperty("所属公司ID")
    private String companyId;
    @ApiModelProperty("合同编号")
    private String contractNumber;
    @ApiModelProperty("合同状态审批状态：0：不通过 1：通过2：审核中")
    private int auditAype;
    @ApiModelProperty("合同PDF地址")
    private String conractUrlPdf;
    @ApiModelProperty("合同签订时间")
    private Date signTime;
}
