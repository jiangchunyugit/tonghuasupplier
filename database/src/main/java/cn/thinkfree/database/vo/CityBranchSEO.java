package cn.thinkfree.database.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 城市分站管理查询条件  继承分页
 */
@ApiModel(description = "城市分站管理条件")
public class CityBranchSEO extends AbsPageSearchCriteria  {

    @ApiModelProperty("分站状态")
    private Integer isEnable;

    @ApiModelProperty("法人名称")
    private String legalName;

    @ApiModelProperty("法人电话号码")
    private String legalPhone;

    @ApiModelProperty("分公司id")
    private Integer branchCompanyId;

    @ApiModelProperty("城市code")
    private Integer cityCode;

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
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

    public Integer getBranchCompanyId() {
        return branchCompanyId;
    }

    public void setBranchCompanyId(Integer branchCompanyId) {
        this.branchCompanyId = branchCompanyId;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
}