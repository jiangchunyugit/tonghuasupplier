package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.model.StoreInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经营主体信息
 */
@ApiModel("经营主体信息")
public class BusinessEntityVO extends BusinessEntity {

    @ApiModelProperty("分公司名称")
    private String ebsBranchCompanyNm;

    @ApiModelProperty("省分站名称")
    private String branchCompanyNm;

    @ApiModelProperty("分站信息")
    private List<BusinessEntityRelationVO> businessEntityRelationVOS;

    public List<BusinessEntityRelationVO> getBusinessEntityRelationVOS() {
        return businessEntityRelationVOS;
    }

    public void setBusinessEntityRelationVOS(List<BusinessEntityRelationVO> businessEntityRelationVOS) {
        this.businessEntityRelationVOS = businessEntityRelationVOS;
    }

    public String getEbsBranchCompanyNm() {
        return ebsBranchCompanyNm;
    }

    public void setEbsBranchCompanyNm(String ebsBranchCompanyNm) {
        this.ebsBranchCompanyNm = ebsBranchCompanyNm;
    }

    public String getBranchcompanyNm() {
        return branchCompanyNm;
    }

    public void setBranchcompanyNm(String branchcompanyNm) {
        this.branchCompanyNm = branchcompanyNm;
    }
}
