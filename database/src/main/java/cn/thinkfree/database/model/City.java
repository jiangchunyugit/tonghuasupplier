package cn.thinkfree.database.model;

import cn.thinkfree.core.model.BaseModel;

/**
 * Database Table Remarks:
 *   城市
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table city
 */
public class City extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city.city_code
     *
     * @mbg.generated
     */
    private String cityCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city.city_name
     *
     * @mbg.generated
     */
    private String cityName;

    /**
     * Database Column Remarks:
     *   所属省份编码
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column city.province_code
     *
     * @mbg.generated
     */
    private String provinceCode;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city.id
     *
     * @return the value of city.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city.id
     *
     * @param id the value for city.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city.city_code
     *
     * @return the value of city.city_code
     *
     * @mbg.generated
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city.city_code
     *
     * @param cityCode the value for city.city_code
     *
     * @mbg.generated
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city.city_name
     *
     * @return the value of city.city_name
     *
     * @mbg.generated
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city.city_name
     *
     * @param cityName the value for city.city_name
     *
     * @mbg.generated
     */
    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column city.province_code
     *
     * @return the value of city.province_code
     *
     * @mbg.generated
     */
    public String getProvinceCode() {
        return provinceCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column city.province_code
     *
     * @param provinceCode the value for city.province_code
     *
     * @mbg.generated
     */
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }
}