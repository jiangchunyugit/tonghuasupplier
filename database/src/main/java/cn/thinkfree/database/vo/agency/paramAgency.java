package cn.thinkfree.database.vo.agency;

import cn.thinkfree.database.model.AgencyContract;
import cn.thinkfree.database.model.AgencyContractTerms;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "经销商合同新增入参")
public class paramAgency  {


    public AgencyContract getAgencyContract() {
        return agencyContract;
    }

    public void setAgencyContract(AgencyContract agencyContract) {
        this.agencyContract = agencyContract;
    }

    @ApiModelProperty(value="合同信息")
    private AgencyContract agencyContract;

    public AgencyContractTerms getAgencyContractTerms() {
        return agencyContractTerms;
    }

    public void setAgencyContractTerms(AgencyContractTerms agencyContractTerms) {
        this.agencyContractTerms = agencyContractTerms;
    }

    @ApiModelProperty(value="品牌信息")
    private AgencyContractTerms  agencyContractTerms;

}
