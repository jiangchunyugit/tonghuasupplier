package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 公司列表参数
 */
@ApiModel("公司列表参数")
public class CompanyListSEO extends AbsPageSearchCriteria  {

    @ApiModelProperty(value = "经销商列表筛选：开始时间")
    private String startDate;

    @ApiModelProperty(value = "经销商列表筛选：结束时间")
    private String endDate;

    @ApiModelProperty(value = "经销商公司编号")
    private String dealerCompanyId;
    /**
     * 公司编号
     */
    @ApiModelProperty(value = "公司编号")
    private String companyId;
    /**
     * 入驻日期
     */
    @ApiModelProperty(value = "入驻日期")
    private String startTime;
    /**
     * 公司类型
     */
    @ApiModelProperty(value = "公司类型")
    private String roleId;

    /**
     * 公司名称，法人名称，联系人电话，联系人姓名 模糊查询
     */
    @ApiModelProperty(value = "公司名称，法人名称，联系人电话，联系人姓名 模糊查询")
    private String param;

    /**
     * 状态：0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金6已交保证金 7入驻成功 8资质待审核 9资质审核通过 10资质审核不通过';
     */
    @ApiModelProperty(value = "状态：0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金6已交保证金 7入驻成功 8资质待审核 9资质审核通过 10资质审核不通过';")
    private String auditStatus;
    /**
     * 签约日期
     */
    @ApiModelProperty(value = "签约日期")
    private String signedTime;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String provinceCode;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String cityCode;

    /**
     * 站点
     */
    @ApiModelProperty(value = "站点")
    private String siteCode;

    /**
     * 相关公司：登陆的运营人员相关联的公司
     */
    @ApiModelProperty(value = "相关公司：登陆的运营人员相关联的公司")
    private List<String> relationMap;

    /**
     * 公司级别：一级，二级，三级
     */
    @ApiModelProperty(value = "公司级别：一级，二级，三级")
    private String companyClassify;

    /**
     * 导出条件项
     * @return
     */
    @ApiModelProperty(value = "导出条件项")
    private List<String> companyIds;

    /**
     * 排序字段
     * @return
     */
    @ApiModelProperty(value = "排序字段")
    Map<String, String> orderList;

    @ApiModelProperty(value = "分公司code")
    private String branchCompanyCode;

    @ApiModelProperty(value = "城市分站code")
    private String cityBranchCode;

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

    public String getDealerCompanyId() {
        return dealerCompanyId;
    }

    public void setDealerCompanyId(String dealerCompanyId) {
        this.dealerCompanyId = dealerCompanyId;
    }

    public String getBranchCompanyCode() {
        return branchCompanyCode;
    }

    public void setBranchCompanyCode(String branchCompanyCode) {
        this.branchCompanyCode = branchCompanyCode;
    }


    public String getCityBranchCode() {
        return cityBranchCode;
    }

    public void setCityBranchCode(String cityBranchCode) {
        this.cityBranchCode = cityBranchCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(String signedTime) {
        this.signedTime = signedTime;
    }

    public String getCompanyClassify() {
        return companyClassify;
    }

    public void setCompanyClassify(String companyClassify) {
        this.companyClassify = companyClassify;
    }

    public Map<String, String> getOrderList() {
        return orderList;
    }

    public void setOrderList(Map<String, String> orderList) {
        this.orderList = orderList;
    }

    public List<String> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(List<String> companyIds) {
        this.companyIds = companyIds;
    }


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
