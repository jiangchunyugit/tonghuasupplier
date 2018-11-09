package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ContractTerms;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.vo.contract.ContractCostVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel("公司详情")
public class CompanyDetailsVO {

    @ApiModelProperty(value = "审批信息")
    private List<PcAuditInfo> pcAuditInfo;
    /**
     * 公司详情
     */
    @ApiModelProperty(value = "公司信息")
    private CompanySubmitVo companySubmitVO;

    /**
     * 结算比例信息
     */
    @ApiModelProperty(value = "结算比例信息")
    private List<ContractCostVo> contractTermsList;

    public CompanySubmitVo getCompanySubmitVO() {
        return companySubmitVO;
    }

    public void setCompanySubmitVO(CompanySubmitVo companySubmitVO) {
        this.companySubmitVO = companySubmitVO;
    }

    public List<ContractCostVo> getContractTermsList() {
        return contractTermsList;
    }

    public void setContractTermsList(List<ContractCostVo> contractTermsList) {
        this.contractTermsList = contractTermsList;
    }

    public List<PcAuditInfo> getPcAuditInfo() {
        return pcAuditInfo;
    }

    public void setPcAuditInfo(List<PcAuditInfo> pcAuditInfo) {
        this.pcAuditInfo = pcAuditInfo;
    }
}
