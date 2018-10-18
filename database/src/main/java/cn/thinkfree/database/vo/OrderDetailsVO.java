package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

/**
 * @Auther: jiang
 * @Date: 2018/10/17 13:39
 * @Description: 订单详情
 */
@ApiModel(description = "订单详情")
public class OrderDetailsVO {

    @ApiModelProperty("项目编号")
    private String projectNo;
    //阶段
    @ApiModelProperty("项目阶段(预约/量房/设计/施工)")
    private Integer stage;
    @ApiModelProperty("发布时间")
    private Date releaseTime;
    //订单信息
    @ApiModelProperty("所属区域")
    private String address;
    @ApiModelProperty("装饰公司")
    private String companyName;
    @ApiModelProperty("订单编号")
    private String orderNo;
    @ApiModelProperty("创建时间")
    private Date createDate;
    @ApiModelProperty("关闭时间")
    private Date closingTime;
    @ApiModelProperty("订单阶段")
    private String orderPhase;
    @ApiModelProperty("订单状态")
    private Integer orderStage;
    @ApiModelProperty("订单来源")
    private Integer orderSource;

    //业主信息
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("业主")
    private String consumerName;
    @ApiModelProperty("项目地址")
    private String projectAddress;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("修改缩略图")
    private String orderUpdateThumbnail;

    //房屋信息
    @ApiModelProperty("房屋类型")
    private Integer houseType;
    @ApiModelProperty("装饰预算")
    private Integer decorationBudget;
    //户型:卧 厅 卫
    @ApiModelProperty("卧室")
    private Integer houseRoom;
    @ApiModelProperty("客厅")
    private Integer houseOffice;
    @ApiModelProperty("卫生间")
    private Integer houseToilet;
    @ApiModelProperty("阳台")
    private Integer houseBalcony;
    @ApiModelProperty("面积")
    private Integer area;
    @ApiModelProperty("装饰风格")
    private Integer style;
    @ApiModelProperty("参考方案")
    private String referencePlan;
    @ApiModelProperty("备注")
    private String remark;


    //设计信息
    @ApiModelProperty("设计是否完成")
    private Integer isComplete;
    @ApiModelProperty("设计师")
    private String designerName;
    @ApiModelProperty("图纸报价审核状态")
    private Integer approvalStatus;
    @ApiModelProperty("提交审核时间")
    private Date reviewTime;
    @ApiModelProperty("修改缩略图")
    private String designerUpdateThumbnail;
    @ApiModelProperty("方案图")
    private String blueprint;


    //施工信息
    @ApiModelProperty("方案图")
    private Integer decorationType;
    @ApiModelProperty("施工周期开始时间")
    private Date startDate;
    @ApiModelProperty("施工周期结束时间")
    private Date endDate;
    @ApiModelProperty("施工阶段")
    private Integer constructionStage;
    @ApiModelProperty("合同金额")
    private Integer contractAmount;
    @ApiModelProperty("项目经理")
    private String projectManager;
    @ApiModelProperty("工长")
    private String foreman;
    @ApiModelProperty("管家")
    private String housekeeper;
    @ApiModelProperty("质检")
    private String qualityInspection;
    @ApiModelProperty("修改缩略图")
    private String constructionThumbnail;

    @ApiModelProperty("状态")
    private Integer status;

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
    }

    public String getOrderPhase() {
        return orderPhase;
    }

    public void setOrderPhase(String orderPhase) {
        this.orderPhase = orderPhase;
    }

    public Integer getOrderStage() {
        return orderStage;
    }

    public void setOrderStage(Integer orderStage) {
        this.orderStage = orderStage;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderUpdateThumbnail() {
        return orderUpdateThumbnail;
    }

    public void setOrderUpdateThumbnail(String orderUpdateThumbnail) {
        this.orderUpdateThumbnail = orderUpdateThumbnail;
    }

    public Integer getHouseType() {
        return houseType;
    }

    public void setHouseType(Integer houseType) {
        this.houseType = houseType;
    }

    public Integer getDecorationBudget() {
        return decorationBudget;
    }

    public void setDecorationBudget(Integer decorationBudget) {
        this.decorationBudget = decorationBudget;
    }

    public Integer getHouseRoom() {
        return houseRoom;
    }

    public void setHouseRoom(Integer houseRoom) {
        this.houseRoom = houseRoom;
    }

    public Integer getHouseOffice() {
        return houseOffice;
    }

    public void setHouseOffice(Integer houseOffice) {
        this.houseOffice = houseOffice;
    }

    public Integer getHouseToilet() {
        return houseToilet;
    }

    public void setHouseToilet(Integer houseToilet) {
        this.houseToilet = houseToilet;
    }

    public Integer getHouseBalcony() {
        return houseBalcony;
    }

    public void setHouseBalcony(Integer houseBalcony) {
        this.houseBalcony = houseBalcony;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }

    public String getReferencePlan() {
        return referencePlan;
    }

    public void setReferencePlan(String referencePlan) {
        this.referencePlan = referencePlan;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getDesignerUpdateThumbnail() {
        return designerUpdateThumbnail;
    }

    public void setDesignerUpdateThumbnail(String designerUpdateThumbnail) {
        this.designerUpdateThumbnail = designerUpdateThumbnail;
    }

    public String getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(String blueprint) {
        this.blueprint = blueprint;
    }

    public Integer getDecorationType() {
        return decorationType;
    }

    public void setDecorationType(Integer decorationType) {
        this.decorationType = decorationType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getConstructionStage() {
        return constructionStage;
    }

    public void setConstructionStage(Integer constructionStage) {
        this.constructionStage = constructionStage;
    }

    public Integer getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Integer contractAmount) {
        this.contractAmount = contractAmount;
    }

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

    public String getConstructionThumbnail() {
        return constructionThumbnail;
    }

    public void setConstructionThumbnail(String constructionThumbnail) {
        this.constructionThumbnail = constructionThumbnail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
