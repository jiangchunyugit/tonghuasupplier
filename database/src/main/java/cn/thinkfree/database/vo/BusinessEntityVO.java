package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BusinessEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class BusinessEntityVO extends BusinessEntity {

    @ApiModelProperty("分公司名称")
    private String branchCompanyNm;

    @ApiModelProperty("城市分站名称")
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
