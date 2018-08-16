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
