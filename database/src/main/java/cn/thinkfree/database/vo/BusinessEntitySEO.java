package cn.thinkfree.database.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经营主体管理查询条件  继承分页
 */
@ApiModel(description = "子公司管理条件")
public class BusinessEntitySEO extends AbsPageSearchCriteria  {

    @ApiModelProperty("分公司编号")
    private String branchCompanyCode;

    @ApiModelProperty("城市分站编号")
    private String cityBranchCode;

    @ApiModelProperty("启动状态")
    private Integer isEnable;

    @ApiModelProperty("经营主体名称")
    private String businessEntityNm;

    public String getBranchCompanyCode() {
        return branchCompanyCode;
    }

    public void setBranchCompanyCode(String branchCompanyCode) {
        this.branchCompanyCode = branchCompanyCode;
    }

    public String getCityBranchCode() {
        return cityBranchCode;
    }

    public void setCityBranchCode(String cityBranchCode) {
        this.cityBranchCode = cityBranchCode;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public String getBusinessEntityNm() {
        return businessEntityNm;
    }

    public void setBusinessEntityNm(String businessEntityNm) {
        this.businessEntityNm = businessEntityNm;
    }
}
