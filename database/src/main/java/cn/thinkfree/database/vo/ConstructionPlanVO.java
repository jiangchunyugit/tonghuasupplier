package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Auther: jiang
 * @Date: 2018/10/22 14:15
 * @Description: 施工计划
 */
@ApiModel(description = "施工计划")
public class ConstructionPlanVO {
    @ApiModelProperty("施工阶段")
    private Integer stage;
    @ApiModelProperty("施工项目")
    private String constructionProject;
    @ApiModelProperty("计划开始时间")
    private Date planSatrtTime;
    @ApiModelProperty("计划结束时间")
    private Date planEndTime;
    @ApiModelProperty("实际开始时间")
    private Date actualSatrtTime;
    @ApiModelProperty("实际结束时间")
    private Date actualEndTime;
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("状态(1,正常  2,失效)")
    private Integer status;

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public String getConstructionProject() {
        return constructionProject;
    }

    public void setConstructionProject(String constructionProject) {
        this.constructionProject = constructionProject;
    }

    public Date getPlanSatrtTime() {
        return planSatrtTime;
    }

    public void setPlanSatrtTime(Date planSatrtTime) {
        this.planSatrtTime = planSatrtTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public Date getActualSatrtTime() {
        return actualSatrtTime;
    }

    public void setActualSatrtTime(Date actualSatrtTime) {
        this.actualSatrtTime = actualSatrtTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
