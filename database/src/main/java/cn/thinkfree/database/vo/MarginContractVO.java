package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu(后台)
 * @date 20181109
 * @Description 保证金待结算数据池应收费用单（合同提供）
 */
@ApiModel("保证金待结算数据池应收费用单（合同提供）")
public class MarginContractVO {

    @ApiModelProperty("总金额---合同保证金")
    private String amount;

    @ApiModelProperty("合同号")
    private String contractNumber;

    @ApiModelProperty("摘要")
    private String description;

    @ApiModelProperty("业务实体")
    private String operatingUnit;

    @ApiModelProperty("业务人员--审批合同")
    private String operationPerson;

    @ApiModelProperty("业务类型")
    private String optionType;

    @ApiModelProperty("事务处理日期--签约时间")
    private String transactionDate;

    @ApiModelProperty("事务处理编号")
    private String transactionId;

    @ApiModelProperty("交易方编码--公司编号")
    private String vendorId;

    @ApiModelProperty("交易方名称--公司名称")
    private String vendorName;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperatingUnit() {
        return operatingUnit;
    }

    public void setOperatingUnit(String operatingUnit) {
        this.operatingUnit = operatingUnit;
    }

    public String getOperationPerson() {
        return operationPerson;
    }

    public void setOperationPerson(String operationPerson) {
        this.operationPerson = operationPerson;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorSiteNumber() {
        return vendorSiteNumber;
    }

    public void setVendorSiteNumber(String vendorSiteNumber) {
        this.vendorSiteNumber = vendorSiteNumber;
    }

    @ApiModelProperty("交易方地点编号")

    private String vendorSiteNumber;
}
