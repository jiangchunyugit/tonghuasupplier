package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("地区")
public class StaffSEO extends AbsPageSearchCriteria{

    /*
     * 省
     * */
    @ApiModelProperty("省编号")
    private String province_code;

    /*
     * 市
     * */
    @ApiModelProperty("市编号")
    private String city_code;

    /*
     *县编号
     * */
    @ApiModelProperty("县编号")
    private String area_code;

    /**
     * 公司编号
     */
    @ApiModelProperty("公司编号")
    private String company_id;

    /*
     * 公司名称
     * */
    @ApiModelProperty("公司名称")
    private String company_name;

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
