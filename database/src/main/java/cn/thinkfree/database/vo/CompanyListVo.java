package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CompanyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 公司列表返回数据
 */
@ApiModel("公司列表返回数据")
public class CompanyListVo extends CompanyInfo {

    @ApiModelProperty(value = "审核和变更中的品牌数量")
    private String brandCount;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String provinceName;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String cityName;

    @ApiModelProperty(value = "市")
    private String areaName;

    /**
     * 公司性质
     */
    @ApiModelProperty(value = "公司性质")
    private Short comapnyNature;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String siteProvinceCode;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String siteProvinceName;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String siteCityCode;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String siteCityName;
    /**
     * 站点
     */
    @ApiModelProperty(value = "站点")
    private String siteCode;
    /**
     * 站点
     */
    @ApiModelProperty(value = "站点")
    private String siteName;
    /**
     * 入驻时间
     */
    @ApiModelProperty(value = "入驻时间")
    private Date startTime;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    private Date endTime;
    /**
     * 签约日期
     */
    @ApiModelProperty(value = "签约日期")
    private Date signedTime;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactName;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

    /**
     * 公司类型
     */
    @ApiModelProperty(value = "公司类型")
    private String roleName;

    @ApiModelProperty(value = "角色id")
    private  String roleId;

    @ApiModelProperty(value = "品牌")
    private  String brandName;

    public String getBrandCount() {
        return brandCount;
    }

    public void setBrandCount(String brandCount) {
        this.brandCount = brandCount;
    }

    public String getSiteProvinceCode() {
        return siteProvinceCode;
    }

    public void setSiteProvinceCode(String siteProvinceCode) {
        this.siteProvinceCode = siteProvinceCode;
    }

    public String getSiteCityCode() {
        return siteCityCode;
    }

    public void setSiteCityCode(String siteCityCode) {
        this.siteCityCode = siteCityCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Short getComapnyNature() {
        return comapnyNature;
    }

    public void setComapnyNature(Short comapnyNature) {
        this.comapnyNature = comapnyNature;
    }


    public String getSiteProvinceName() {
        return siteProvinceName;
    }

    public void setSiteProvinceName(String siteProvinceName) {
        this.siteProvinceName = siteProvinceName;
    }

    public String getSiteCityName() {
        return siteCityName;
    }

    public void setSiteCityName(String siteCityName) {
        this.siteCityName = siteCityName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(Date signedTime) {
        this.signedTime = signedTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    @Override
    public String toString() {
        return "CompanyListVo{" +
                "comapnyNature=" + comapnyNature +
                ", siteProvinceName='" + siteProvinceName + '\'' +
                ", siteCityName='" + siteCityName + '\'' +
                ", siteCode=" + siteCode +
                ", siteName='" + siteName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", signedTime=" + signedTime +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contractNumber='" + contractNumber + '\'' +
                '}';
    }
}
