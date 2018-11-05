package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 设计订单列表
 */
public class DesignerOrderVo {
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("设计订单编号")
    private String designOrderNo;
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String ownerPhone;
    @ApiModelProperty("所在地")
    private String address;
    @ApiModelProperty("订单来源")
    private String orderSource;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("装修风格")
    private String styleName;
    @ApiModelProperty("装修预算")
    private String budget;
    @ApiModelProperty("建筑面积")
    private String area;
    @ApiModelProperty("归属设计公司名称")
    private String companyName;
    @ApiModelProperty("公司状态")
    private String companyState;
    @ApiModelProperty("归属设计师")
    private String designerName;
    @ApiModelProperty("订单状态名称")
    private String orderStateName;
    @ApiModelProperty("操作人姓名")
    private String optionUserName;
    @ApiModelProperty("操作时间")
    private String optionTime;
    @ApiModelProperty("订单状态")
    private String orderState;
    @ApiModelProperty("项目总额（订单总额）")
    private String projectMoney;

    public DesignerOrderVo() {
    }

    public DesignerOrderVo(String projectNo, String designOrderNo, String ownerName, String ownerPhone, String address,
                           String orderSource, String createTime, String styleName, String budget, String area, String companyName,
                           String companyState, String designerName, String orderStateName, String optionUserName, String optionTime,
                           String orderState, String projectMoney) {
        this.projectNo = projectNo;
        this.designOrderNo = designOrderNo;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.address = address;
        this.orderSource = orderSource;
        this.createTime = createTime;
        this.styleName = styleName;
        this.budget = budget;
        this.area = area;
        this.companyName = companyName;
        this.companyState = companyState;
        this.designerName = designerName;
        this.orderStateName = orderStateName;
        this.optionUserName = optionUserName;
        this.optionTime = optionTime;
        this.orderState = orderState;
        this.projectMoney = projectMoney;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getDesignOrderNo() {
        return designOrderNo;
    }

    public void setDesignOrderNo(String designOrderNo) {
        this.designOrderNo = designOrderNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }

    public String getOptionUserName() {
        return optionUserName;
    }

    public void setOptionUserName(String optionUserName) {
        this.optionUserName = optionUserName;
    }

    public String getOptionTime() {
        return optionTime;
    }

    public void setOptionTime(String optionTime) {
        this.optionTime = optionTime;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setProjectMoney(String projectMoney) {
        this.projectMoney = projectMoney;
    }

    public String getProjectMoney() {
        return projectMoney;
    }
}
