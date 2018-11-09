package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ContractTerms;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

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
    private Map<String, Object> contractTermsList;

    public CompanySubmitVo getCompanySubmitVO() {
        return companySubmitVO;
    }

    public void setCompanySubmitVO(CompanySubmitVo companySubmitVO) {
        this.companySubmitVO = companySubmitVO;
    }

    public Map<String, Object> getContractTermsList() {
        return contractTermsList;
    }

    public void setContractTermsList(Map<String, Object> contractTermsList) {
        this.contractTermsList = contractTermsList;
    }
}
