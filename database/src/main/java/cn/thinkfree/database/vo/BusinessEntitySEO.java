package cn.thinkfree.database.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu 经营主体管理查询条件  继承分页
 */
@ApiModel(description = "子公司管理条件")
public class BusinessEntitySEO extends AbsPageSearchCriteria  {
    @ApiModelProperty("埃森哲分公司id")
    private Integer branchCompanyEbsId;

    @ApiModelProperty("埃森哲城市分站id")
    private Integer cityBranchid;

    @ApiModelProperty("启动状态")
    private Integer isEnable;

    @ApiModelProperty("经营主体名称")
    private String businessEntityNm;

    public Integer getBranchCompanyEbsId() {
        return branchCompanyEbsId;
    }

    public void setBranchCompanyEbsId(Integer branchCompanyEbsId) {
        this.branchCompanyEbsId = branchCompanyEbsId;
    }

    public Integer getCityBranchid() {
        return cityBranchid;
    }

    public void setCityBranchid(Integer cityBranchid) {
        this.cityBranchid = cityBranchid;
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
