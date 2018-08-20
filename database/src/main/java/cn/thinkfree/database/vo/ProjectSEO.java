package cn.thinkfree.database.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
@ApiModel
public class ProjectSEO extends AbsPageSearchCriteria {

    /**
     * 省
     */
    private String province;
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
    private String status;
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
    @ApiModelProperty(hidden = true)
    private String companyID;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private List<String> companyRelationMap;

    /**
     * 后台查询使用
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private List<String> filterProjectNos;

    public List<String> getCompanyRelationMap() {
        return companyRelationMap;
    }

    public void setCompanyRelationMap(List<String> companyRelationMap) {
        this.companyRelationMap = companyRelationMap;
    }

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
