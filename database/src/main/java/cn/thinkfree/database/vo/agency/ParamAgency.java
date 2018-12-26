package cn.thinkfree.database.vo.agency;

import cn.thinkfree.database.model.AgencyContract;
import cn.thinkfree.database.model.AgencyContractTerms;
import cn.thinkfree.database.model.PcAuditInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商合同详情
 */
@ApiModel(description = "经销商合同详情")
public class ParamAgency  {

    @ApiModelProperty(value="合同信息")
    private AgencyContract agencyContract;

    @ApiModelProperty(value="品牌信息")
    private List<AgencyContractTerms>  agencyContractTermsList;

    @ApiModelProperty("审核信息")
    private List<PcAuditInfo> auditInfo;

    public List<AgencyContractTerms> getAgencyContractTermsList() {
        return agencyContractTermsList;
    }

    public void setAgencyContractTermsList(List<AgencyContractTerms> agencyContractTermsList) {
        this.agencyContractTermsList = agencyContractTermsList;
    }

    public List<PcAuditInfo> getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(List<PcAuditInfo> auditInfo) {
        this.auditInfo = auditInfo;
    }

    public AgencyContract getAgencyContract() {
        return agencyContract;
    }

    public void setAgencyContract(AgencyContract agencyContract) {
        this.agencyContract = agencyContract;
    }
}
