package cn.thinkfree.database.vo.agency;

import cn.thinkfree.database.model.AgencyContract;
import cn.thinkfree.database.model.AgencyContractTerms;
import cn.thinkfree.database.vo.Severitys;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商合同新增入参
 */
@ApiModel(description = "经销商合同新增入参")
public class ParamAgencySEO extends AgencyContract{

    @ApiModelProperty(value="品牌信息")
    @NotEmpty(message = "品牌，品类不可以为空",groups = {Severitys.Insert.class})
    private List<AgencyContractTerms> agencyContractTermsList;

    public List<AgencyContractTerms> getAgencyContractTermsList() {
        return agencyContractTermsList;
    }

    public void setAgencyContractTermsList(List<AgencyContractTerms> agencyContractTermsList) {
        this.agencyContractTermsList = agencyContractTermsList;
    }
}
