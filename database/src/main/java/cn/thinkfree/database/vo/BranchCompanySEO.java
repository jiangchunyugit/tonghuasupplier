package cn.thinkfree.database.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 子公司管理查询条件  继承分页
 */
@ApiModel(description = "子公司管理条件")
public class BranchCompanySEO extends AbsPageSearchCriteria  {

    @ApiModelProperty("分站状态")
    private Integer isEnable;

    @ApiModelProperty("法人名称")
    private String legalName;

    @ApiModelProperty("法人电话号码")
    private String legalPhone;

    @ApiModelProperty("分公司名称")
    private String branchCompanyName;

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalPhone() {
        return legalPhone;
    }

    public void setLegalPhone(String legalPhone) {
        this.legalPhone = legalPhone;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public String getBranchCompanyName() {
        return branchCompanyName;
    }

    public void setBranchCompanyName(String branchCompanyName) {
        this.branchCompanyName = branchCompanyName;
    }
}