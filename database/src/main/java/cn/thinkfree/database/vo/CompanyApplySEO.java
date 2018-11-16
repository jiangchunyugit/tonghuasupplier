package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author ying007
 * 申请信息查询参数
 */
@ApiModel(description = "申请信息查询参数")
public class CompanyApplySEO extends AbsPageSearchCriteria {

    /**
     * 公司名称，联系人姓名，手机号
     */
    @ApiModelProperty(value = "公司名称，联系人姓名，手机号")
    private String param;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "申请事项")
    private String applyThingType;

    @ApiModelProperty(value = "公司类型")
    private String companyRole;

    @ApiModelProperty(value = "市")
    private String cityCode;

    @ApiModelProperty(value = "区")
    private Integer areaCode;

    @ApiModelProperty(value = "省")
    private String provinceCode;

    @ApiModelProperty(value = "申请来源")
    private String applyType;

    @ApiModelProperty(value = "办理状态")
    private String transactType;

    public String getTransactType() {
        return transactType;
    }

    public void setTransactType(String transactType) {
        this.transactType = transactType;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getApplyThingType() {
        return applyThingType;
    }

    public void setApplyThingType(String applyThingType) {
        this.applyThingType = applyThingType;
    }

    public String getCompanyRole() {
        return companyRole;
    }

    public void setCompanyRole(String companyRole) {
        this.companyRole = companyRole;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    @Override
    public String toString() {
        return "CompanyApplySEO{" +
                "param='" + param + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", applyThingType=" + applyThingType +
                ", companyRole='" + companyRole + '\'' +
                ", cityCode=" + cityCode +
                ", areaCode=" + areaCode +
                ", provinceCode=" + provinceCode +
                '}';
    }
}
