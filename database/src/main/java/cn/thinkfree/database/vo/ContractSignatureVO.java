package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ContractSignature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "合同签名信息")
public class ContractSignatureVO {

    @ApiModelProperty(value="填写合同编号")
    private String contractNo;

    @ApiModelProperty(value="填写合同签名URLlist")
    private List<ContractSignature> contractSignatures;

    public List<ContractSignature> getContractSignatures() {
        return contractSignatures;
    }

    public void setContractSignatures(List<ContractSignature> contractSignatures) {
        this.contractSignatures = contractSignatures;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
}
