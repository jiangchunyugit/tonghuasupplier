package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CityBranch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 城市分站信息
 */
@ApiModel("城市分站信息")
public class CityBranchVO extends CityBranch {

    @ApiModelProperty("分公司名称")
    private String ebsBranchCompanyNm;

    @ApiModelProperty("省分站名称")
    private String branchCompanyNm;

    @ApiModelProperty("城市名称")
    private String cityNm;

    @ApiModelProperty("省份名称")
    private String provinceNm;

    @ApiModelProperty("门店数")
    private String count;

    @ApiModelProperty("经营主体：门店")
    @NotEmpty(message = "门店不可为空",groups = {Severitys.Insert.class,Severitys.Update.class})
    private List<StoreInfoVO> storeInfoVOList;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getEbsBranchCompanyNm() {
        return ebsBranchCompanyNm;
    }

    public void setEbsBranchCompanyNm(String ebsBranchCompanyNm) {
        this.ebsBranchCompanyNm = ebsBranchCompanyNm;
    }

    public String getProvinceNm() {
        return provinceNm;
    }

    public void setProvinceNm(String provinceNm) {
        this.provinceNm = provinceNm;
    }

    public List<StoreInfoVO> getStoreInfoVOList() {
        return storeInfoVOList;
    }

    public void setStoreInfoVOList(List<StoreInfoVO> storeInfoVOList) {
        this.storeInfoVOList = storeInfoVOList;
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
