package cn.thinkfree.database.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

/**
 * 子公司管理查询条件  继承分页
 */
@ApiModel(description = "子公司管理条件")
public class CompanyInfoSEO extends AbsPageSearchCriteria  {
    @ApiModelProperty("省id")
    private String provinceCode;

    @ApiModelProperty("市id")
    private String cityCode;

    @ApiModelProperty("区id")
    private String areaCode;

    @ApiModelProperty("公司id,不用这个字段")
    private String companyId;

    @ApiModelProperty("法人名称")
    @Max(value = 10, message = "负责人姓名最长不超过10个字")
    private String legalName;

    @ApiModelProperty("法人电话号码")
    @Max(value = 11, message = "负责人手机号最长不超过10个字")
    private String legalPhone;

    @Max(value = 23, message = "分公司名称最长不超过23个字")
    @ApiModelProperty("公司名称")
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalPhone() {
        return legalPhone;
    }

    public void setLegalPhone(String legalPhone) {
        this.legalPhone = legalPhone;
    }
}
