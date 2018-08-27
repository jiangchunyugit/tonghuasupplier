package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目详情
 */
@ApiModel("项目交互详情")
public class ProjectVO extends PreProjectGuide {

    @ApiModelProperty("公司信息")
    private PreProjectCompanySet preProjectCompanySet;
    @ApiModelProperty("项目信息")
    private PreProjectInfo preProjectInfo;
//    @ApiModelProperty("项目状态")
//    private PreProjectStatus preProjectStatus;
    @ApiModelProperty("项目员工")
    private PreProjectUserRole preProjectUserRole;

    /**
     * 管家姓名
     */
    @ApiModelProperty("管家姓名")
    private String stewardName;

    /**
     * 项目经理名称
     */
    @ApiModelProperty("项目经理姓名")
    private String projectManagerName;

    /**
     * 所属公司
     * @return
     */
    @ApiModelProperty("所属公司名称")
    public String companyName;

    /**
     * 户型名称
     */
    @ApiModelProperty("户型")
    private String houseTypeName;



    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

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

//    public PreProjectStatus getPreProjectStatus() {
//        return preProjectStatus;
//    }
//
//    public void setPreProjectStatus(PreProjectStatus preProjectStatus) {
//        this.preProjectStatus = preProjectStatus;
//    }

    public PreProjectUserRole getPreProjectUserRole() {
        return preProjectUserRole;
    }

    public void setPreProjectUserRole(PreProjectUserRole preProjectUserRole) {
        this.preProjectUserRole = preProjectUserRole;
    }

    public String getHouseTypeName() {
        return houseTypeName;
    }

    public void setHouseTypeName(String houseTypeName) {
        this.houseTypeName = houseTypeName;
    }
}
