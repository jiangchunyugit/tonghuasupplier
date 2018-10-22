package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Auther: jiang
 * @Date: 2018/10/19 16:25
 * @Description:工地详情
 */
@ApiModel(description = "工地详情")
public class SiteDetailsVO {

    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("序号")
    private Integer sort;
    @ApiModelProperty("施工阶段")
    private Integer stage;
    @ApiModelProperty("施工项目")
    private String constructionProject;
    @ApiModelProperty("计划工期开始时间")
    private Date startTime;
    @ApiModelProperty("计划工期结束时间")
    private Date endTime;
    @ApiModelProperty("是否验收")
    private Integer isAcceptance;
    @ApiModelProperty("是否支付")
    private Integer isPay;
    @ApiModelProperty("文字详情")
    private String textDetails;
    @ApiModelProperty("图片详情")
    private String pictureDetails;
    @ApiModelProperty("状态")
    private Integer status;
    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

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

    public Integer getIsAcceptance() {
        return isAcceptance;
    }

    public void setIsAcceptance(Integer isAcceptance) {
        this.isAcceptance = isAcceptance;
    }

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    public String getTextDetails() {
        return textDetails;
    }

    public void setTextDetails(String textDetails) {
        this.textDetails = textDetails;
    }

    public String getPictureDetails() {
        return pictureDetails;
    }

    public void setPictureDetails(String pictureDetails) {
        this.pictureDetails = pictureDetails;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
