package cn.thinkfree.database.vo;

import java.sql.Date;
import java.util.List;

/**
 * 公司列表参数
 */
public class CompanyListSEO extends AbsPageSearchCriteria  {
    /**
     * 公司编号
     */
    private String companyId;
    /**
     * 入驻日期
     */
    private Date startTime;
    /**
     * 公司类型
     */
    private String roleId;

    /**
     * 公司名称，法人名称，联系人电话，联系人姓名 模糊查询
     */
    private String param;

    /**
     * 状态：0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金6已交保证金 7入驻成功 8资质待审核 9资质审核通过 10资质审核不通过';
     */
    private short auditStatus;
    /**
     * 签约日期
     */
    private Date signedTime;

    /**
     * 省
     */
    private String provinceCode;

    /**
     * 市
     */
    private String cityCode;

    /**
     * 站点
     */
    private String siteCode;

    /**
     * 相关公司：登陆的运营人员相关联的公司
     */
    private List<String> relationMap;

    /**
     * 公司级别：一级，二级，三级
     */
    private Short companyClassify;

    public Short getCompanyClassify() {
        return companyClassify;
    }

    public void setCompanyClassify(Short companyClassify) {
        this.companyClassify = companyClassify;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public short getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(short auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(Date signedTime) {
        this.signedTime = signedTime;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public List<String> getRelationMap() {
        return relationMap;
    }

    public void setRelationMap(List<String> relationMap) {
        this.relationMap = relationMap;
    }

    @Override
    public String toString() {
        return "CompanyListSEO{" +
                "companyId='" + companyId + '\'' +
                ", startTime=" + startTime +
                ", roleId='" + roleId + '\'' +
                ", param='" + param + '\'' +
                ", auditStatus=" + auditStatus +
                ", signedTime=" + signedTime +
                ", provinceCode='" + provinceCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", siteCode='" + siteCode + '\'' +
                '}';
    }
}
