package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.*;

/**
 * 项目详情
 */
public class ProjectVO extends PreProjectGuide {

    private PreProjectCompanySet preProjectCompanySet;
    private PreProjectInfo preProjectInfo;
    private PreProjectStatus preProjectStatus;
    private PreProjectUserRole preProjectUserRole;

    /**
     * 管家姓名
     */
    private String stewardName;

    /**
     * 项目经理名称
     */
    private String projectManagerName;

    public String getStewardName() {
        return stewardName;
    }

    public void setStewardName(String stewardName) {
        this.stewardName = stewardName;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    public PreProjectCompanySet getPreProjectCompanySet() {
        return preProjectCompanySet;
    }

    public void setPreProjectCompanySet(PreProjectCompanySet preProjectCompanySet) {
        this.preProjectCompanySet = preProjectCompanySet;
    }

    public PreProjectInfo getPreProjectInfo() {
        return preProjectInfo;
    }

    public void setPreProjectInfo(PreProjectInfo preProjectInfo) {
        this.preProjectInfo = preProjectInfo;
    }

    public PreProjectStatus getPreProjectStatus() {
        return preProjectStatus;
    }

    public void setPreProjectStatus(PreProjectStatus preProjectStatus) {
        this.preProjectStatus = preProjectStatus;
    }

    public PreProjectUserRole getPreProjectUserRole() {
        return preProjectUserRole;
    }

    public void setPreProjectUserRole(PreProjectUserRole preProjectUserRole) {
        this.preProjectUserRole = preProjectUserRole;
    }
}
