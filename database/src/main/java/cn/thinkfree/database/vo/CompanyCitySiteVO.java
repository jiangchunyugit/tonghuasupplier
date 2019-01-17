package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther: jiang
 * @Date: 2019/1/10 15:19
 * @Description:
 */
public class CompanyCitySiteVO {
    @ApiModelProperty("分公司")
    private String branchCompanyName;
    @ApiModelProperty("城市分站名称")
    private String cityBranchName;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("公司id")
    private String companyId;

    public String getBranchCompanyName() {
        return branchCompanyName;
    }

    public void setBranchCompanyName(String branchCompanyName) {
        this.branchCompanyName = branchCompanyName;
    }

    public String getCityBranchName() {
        return cityBranchName;
    }

    public void setCityBranchName(String cityBranchName) {
        this.cityBranchName = cityBranchName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
