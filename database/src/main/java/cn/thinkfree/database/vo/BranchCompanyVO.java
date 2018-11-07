package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BranchCompany;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分公司信息
 * @author jiangchunyu
 */
@ApiModel("分公司信息")
public class BranchCompanyVO extends BranchCompany {

    /**
     * 省份名称
     */
    @ApiModelProperty("省份名称")
    private String provinceNm;

//    /**
//     * 城市分站list
//     */
//    @ApiModelProperty("分公司城市分站")
//    private List<CityBranchWtihProCitVO> cityBranchWtihProCitVOS;

//    public List<CityBranchWtihProCitVO> getCityBranchWtihProCitVOS() {
//        return cityBranchWtihProCitVOS;
//    }
//
//    public void setCityBranchWtihProCitVOS(List<CityBranchWtihProCitVO> cityBranchWtihProCitVOS) {
//        this.cityBranchWtihProCitVOS = cityBranchWtihProCitVOS;
//    }

    public String getProvinceNm() {
        return provinceNm;
    }

    public void setProvinceNm(String provinceNm) {
        this.provinceNm = provinceNm;
    }
}
