package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CityBranch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 城市分站带有省份城市名称
 */
@ApiModel("城市分站带有省份城市名称信息")
public class CityBranchWtihProCitVO extends CityBranch {

    @ApiModelProperty("省份名称")
    private String provinceNm;

    @ApiModelProperty("城市名称")
    private String cityNm;

    public String getProvinceNm() {
        return provinceNm;
    }

    public void setProvinceNm(String provinceNm) {
        this.provinceNm = provinceNm;
    }

    public String getCityNm() {
        return cityNm;
    }

    public void setCityNm(String cityNm) {
        this.cityNm = cityNm;
    }
}
