package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CityBranch;

public class CityBranchWtihProCitVO extends CityBranch {

    /**
     * 省份名称
     */
    private String provinceNm;

    /**
     * 城市名称
     */
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
