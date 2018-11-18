package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BranchCompany;
import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.model.CityBranch;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 *@author jiangchunyu
 * @date 2018
 * @desciption 提供徐洋 分公司，城市分站，经营主体，门店信息
 */
public class CompanyOrganizationVO {

    @ApiModelProperty("分公司编号")
    private BranchCompany branchCompany;

    @ApiModelProperty("城市分站编号")
    private CityBranch cityBranch;

    @ApiModelProperty("经营主体编号")
    private List<BusinessEntityVO> businessEntityVOList;

    public BranchCompany getBranchCompany() {
        return branchCompany;
    }

    public void setBranchCompany(BranchCompany branchCompany) {
        this.branchCompany = branchCompany;
    }

    public CityBranch getCityBranch() {
        return cityBranch;
    }

    public void setCityBranch(CityBranch cityBranch) {
        this.cityBranch = cityBranch;
    }

    public List<BusinessEntityVO> getBusinessEntityVOList() {
        return businessEntityVOList;
    }

    public void setBusinessEntityVOList(List<BusinessEntityVO> businessEntityVOList) {
        this.businessEntityVOList = businessEntityVOList;
    }
}
