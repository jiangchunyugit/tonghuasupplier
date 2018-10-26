package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BranchCompany;

import java.util.List;

public class BranchCompanyVO extends BranchCompany {

    /**
     * 省份名称
     */
    private String provinceNm;

    /**
     * 城市分站list
     */
    private List<CityBranchWtihProCitVO> cityBranchWtihProCitVOS;

    public List<CityBranchWtihProCitVO> getCityBranchWtihProCitVOS() {
        return cityBranchWtihProCitVOS;
    }

    public void setCityBranchWtihProCitVOS(List<CityBranchWtihProCitVO> cityBranchWtihProCitVOS) {
        this.cityBranchWtihProCitVOS = cityBranchWtihProCitVOS;
    }

    public String getProvinceNm() {
        return provinceNm;
    }

    public void setProvinceNm(String provinceNm) {
        this.provinceNm = provinceNm;
    }
}
