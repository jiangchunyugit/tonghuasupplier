package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CompanyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销合同入驻公司信息
 */
@ApiModel(value = "经销合同入驻公司信息")
public class AgencyContractCompanyInfoVo extends CompanyInfo {

    @ApiModelProperty(value="分公司名称")
    private String branchCompanyName;

    @ApiModelProperty(value="城市分站名称")
    private String cityBranchName;

    @ApiModelProperty(value="经营主体名称")
    private String entityName;

    @ApiModelProperty(value = "经营主体编号")
    private String businessEntityCode;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getBusinessEntityCode() {
        return businessEntityCode;
    }

    public void setBusinessEntityCode(String businessEntityCode) {
        this.businessEntityCode = businessEntityCode;
    }
}
