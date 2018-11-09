package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BusinessEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu 经营主体信息
 */
@ApiModel("经营主体信息")
public class BusinessEntityVO extends BusinessEntity {

    @ApiModelProperty("选择分公司")
    private String branchCompanyNm;

    @ApiModelProperty("站点")
    private String cityBranchNm;

    public String getBranchCompanyNm() {
        return branchCompanyNm;
    }

    public void setBranchCompanyNm(String branchCompanyNm) {
        this.branchCompanyNm = branchCompanyNm;
    }

    public String getCityBranchNm() {
        return cityBranchNm;
    }

    public void setCityBranchNm(String cityBranchNm) {
        this.cityBranchNm = cityBranchNm;
    }

    @ApiModelProperty("店面名称")
    private List<String> StoreNm;

    public List<String> getStoreNm() {
        return StoreNm;
    }

    public void setStoreNm(List<String> storeNm) {
        StoreNm = storeNm;
    }
}
