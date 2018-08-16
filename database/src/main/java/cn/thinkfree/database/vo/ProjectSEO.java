package cn.thinkfree.database.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class ProjectSEO extends AbsPageSearchCriteria {

    /**
     * 省
     */
    private String provice;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String area;
    /**
     * 状态
     */
    private String staus;
    /**
     * 客户姓名
     */
    private String customName;
    /**
     * 项目经理
     */
    private String projectManager;
    /**
     * 管家
     */
    private String steward;

    /**
     * 所属店面
     */
    private String hall;

    /**
     * 后台查询使用
     */
    @JsonIgnore
    private String companyID;

    /**
     * 后台查询使用
     */
    @JsonIgnore
    private List<String> filterProjectNos;


    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public List<String> getFilterProjectNos() {
        return filterProjectNos;
    }

    public void setFilterProjectNos(List<String> filterProjectNos) {
        this.filterProjectNos = filterProjectNos;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getSteward() {
        return steward;
    }

    public void setSteward(String steward) {
        this.steward = steward;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }
}
