package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ContractTerms;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("公司详情")
public class CompanyDetailsVO {
    /**
     * 公司详情
     */
    @ApiModelProperty(value = "公司信息")
    private CompanySubmitVo companySubmitVO;

    /**
     * 结算比例信息
     */
    @ApiModelProperty(value = "结算比例信息")
    private List<ContractTerms> contractTermsList;

    public CompanySubmitVo getCompanySubmitVO() {
        return companySubmitVO;
    }

    public void setCompanySubmitVO(CompanySubmitVo companySubmitVO) {
        this.companySubmitVO = companySubmitVO;
    }

    public List<ContractTerms> getContractTermsList() {
        return contractTermsList;
    }

    public void setContractTermsList(List<ContractTerms> contractTermsList) {
        this.contractTermsList = contractTermsList;
    }
}
