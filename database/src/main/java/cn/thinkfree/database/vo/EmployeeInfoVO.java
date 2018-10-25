package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther: jiang
 * @Date: 2018/10/24 13:38
 * @Description:
 */
@ApiModel("员工信息")
public class EmployeeInfoVO {
    @ApiModelProperty("项目经理")
    private String projectManager;
    @ApiModelProperty("工长")
    private String foreman;
    @ApiModelProperty("管家")
    private String housekeeper;
    @ApiModelProperty("质检")
    private String qualityInspection;

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getForeman() {
        return foreman;
    }

    public void setForeman(String foreman) {
        this.foreman = foreman;
    }

    public String getHousekeeper() {
        return housekeeper;
    }

    public void setHousekeeper(String housekeeper) {
        this.housekeeper = housekeeper;
    }

    public String getQualityInspection() {
        return qualityInspection;
    }

    public void setQualityInspection(String qualityInspection) {
        this.qualityInspection = qualityInspection;
    }
}
