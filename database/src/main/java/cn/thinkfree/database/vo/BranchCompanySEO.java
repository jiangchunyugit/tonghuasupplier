package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 分站管理：分公司查询条件  继承分页
 */
@ApiModel(description = "分公司查询条件")
public class BranchCompanySEO extends AbsPageSearchCriteria  {

    @ApiModelProperty("分站状态")
    private Integer isEnable;

    @ApiModelProperty("法人名称")
    private String legalName;

    @ApiModelProperty("法人电话号码")
    private String legalPhone;

    @ApiModelProperty("分公司名称")
    private String branchCompanyName;

    @ApiModelProperty("分公司编号")
    private String branchCompanyCode;

    public String getBranchCompanyCode() {
        return branchCompanyCode;
    }

    public void setBranchCompanyCode(String branchCompanyCode) {
        this.branchCompanyCode = branchCompanyCode;
    }

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