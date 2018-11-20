package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CityBranch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 城市分站信息
 */
@ApiModel("城市分站信息")
public class CityBranchVO extends CityBranch {

    @ApiModelProperty("分公司名称")
    private String branchCompanyNm;

    @ApiModelProperty("城市名称")
    private String cityNm;

    @ApiModelProperty("经营主体：门店")
    private List<BusinessEntityVO> businessEntityVOS;

    public List<BusinessEntityVO> getBusinessEntityVOS() {
        return businessEntityVOS;
    }

    public void setBusinessEntityVOS(List<BusinessEntityVO> businessEntityVOS) {
        this.businessEntityVOS = businessEntityVOS;
    }

    public String getCityNm() {
        return cityNm;
    }

    public void setCityNm(String cityNm) {
        this.cityNm = cityNm;
    }

    public String getBranchCompanyNm() {
        return branchCompanyNm;
    }

    public void setBranchCompanyNm(String branchCompanyNm) {
        this.branchCompanyNm = branchCompanyNm;
    }
}
