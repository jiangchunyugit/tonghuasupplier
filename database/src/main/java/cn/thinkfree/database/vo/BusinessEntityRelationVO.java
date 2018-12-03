package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BusinessEntityRelation;
import cn.thinkfree.database.model.BusinessEntityStore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(value="每个分站下经营主体门店详情")
public class BusinessEntityRelationVO extends BusinessEntityRelation {

    @ApiModelProperty(value="省分站名称")
    private String branchCompanyNm;

    @ApiModelProperty("城市分站名称")
    private String cityBranchNm;

    @ApiModelProperty("经营主体门店")
    private List<BusinessEntityStore> businessEntityStoreList;

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

    public List<BusinessEntityStore> getBusinessEntityStoreList() {
        return businessEntityStoreList;
    }

    public void setBusinessEntityStoreList(List<BusinessEntityStore> businessEntityStoreList) {
        this.businessEntityStoreList = businessEntityStoreList;
    }
}