package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Auther: jiang
 * @Date: 2018/10/15 13:34
 * @Description:项目派单
 */
@ApiModel(description = "项目派单")
public class ProjectOrderVO {
    @ApiModelProperty("所属地区")
    private String address;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("预约日期")
    private Date appointmentTime;
    @ApiModelProperty("签约日期")
    private Date signedTime;
    @ApiModelProperty("项目地址")
    private String addressDetail;
    @ApiModelProperty("业主")
    private String owner;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("折后合同额")
    private Integer reducedContractAmount;
    @ApiModelProperty("已支付")
    private Integer havePaid;
    @ApiModelProperty("订单状态")
    private Integer orderStage;
    @ApiModelProperty("施工进度")
    private String constructionProgress;
    @ApiModelProperty("最近验收情况")
    private Integer checkCondition;
    @ApiModelProperty("延期天数")
    private Integer delayDays;
    @ApiModelProperty("项目经理")
    private String projectManager;
    @ApiModelProperty("设计师")
    private String designerName;
    @ApiModelProperty("订单状态状态(1,正常  2,失效)")
    private Integer status;
    @ApiModelProperty("项目编号")
    private String projectNo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Date getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(Date signedTime) {
        this.signedTime = signedTime;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getReducedContractAmount() {
        return reducedContractAmount;
    }

    public void setReducedContractAmount(Integer reducedContractAmount) {
        this.reducedContractAmount = reducedContractAmount;
    }

    public Integer getHavePaid() {
        return havePaid;
    }

    public void setHavePaid(Integer havePaid) {
        this.havePaid = havePaid;
    }

    public Integer getOrderStage() {
        return orderStage;
    }

    public void setOrderStage(Integer orderStage) {
        this.orderStage = orderStage;
    }

    public String getConstructionProgress() {
        return constructionProgress;
    }

    public void setConstructionProgress(String constructionProgress) {
        this.constructionProgress = constructionProgress;
    }

    public Integer getCheckCondition() {
        return checkCondition;
    }

    public void setCheckCondition(Integer checkCondition) {
        this.checkCondition = checkCondition;
    }

    public Integer getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }
}
