package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 *@author jiangchunyu
 * @date 2018
 * @desciption 提供徐洋 公司类型，分公司，城市分站，经营主体，门店信息
 */
public class EnterCompanyOrganizationVO {

    @ApiModelProperty("入驻公司类型：SJ 设计公司，BD 装饰公司")
    private String companyType;

    @ApiModelProperty("入驻公司名称")
    private String companyNm;

    @ApiModelProperty("分公司编号")
    private String branchCompanyCode;

    @ApiModelProperty("分公司名称")
    private String branchCompanyNm;

    @ApiModelProperty("城市分站编号")
    private String cityBranchCode;

    @ApiModelProperty("城市分站名称")
    private String cityBranchNm;

    @ApiModelProperty("经营主体编号")
    private String businessEntityCode;

    @ApiModelProperty("经营主体名称")
    private String businessEntityNm;

    @ApiModelProperty("门店id")
    private String storeId;

    @ApiModelProperty("门店名称")
    private String storeNm;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreNm() {
        return storeNm;
    }

    public void setStoreNm(String storeNm) {
        this.storeNm = storeNm;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyNm() {
        return companyNm;
    }

    public void setCompanyNm(String companyNm) {
        this.companyNm = companyNm;
    }

    public String getBranchCompanyCode() {
        return branchCompanyCode;
    }

    public void setBranchCompanyCode(String branchCompanyCode) {
        this.branchCompanyCode = branchCompanyCode;
    }

    public String getBranchCompanyNm() {
        return branchCompanyNm;
    }

    public void setBranchCompanyNm(String branchCompanyNm) {
        this.branchCompanyNm = branchCompanyNm;
    }

    public String getCityBranchCode() {
        return cityBranchCode;
    }

    public void setCityBranchCode(String cityBranchCode) {
        this.cityBranchCode = cityBranchCode;
    }

    public String getCityBranchNm() {
        return cityBranchNm;
    }

    public void setCityBranchNm(String cityBranchNm) {
        this.cityBranchNm = cityBranchNm;
    }

    public String getBusinessEntityCode() {
        return businessEntityCode;
    }

    public void setBusinessEntityCode(String businessEntityCode) {
        this.businessEntityCode = businessEntityCode;
    }

    public String getBusinessEntityNm() {
        return businessEntityNm;
    }

    public void setBusinessEntityNm(String businessEntityNm) {
        this.businessEntityNm = businessEntityNm;
    }
}
