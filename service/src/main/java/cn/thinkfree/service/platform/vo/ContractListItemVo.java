package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 */
public class ContractListItemVo {
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("子订单编号")
    private String childNo;
    @ApiModelProperty("创建时间（签约时间）")
    private String createTime;
    @ApiModelProperty("项目地址")
    private String address;
    @ApiModelProperty("订单来源")
    private String orderSource;
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String ownerPhone;
    @ApiModelProperty("金额")
    private String money;
    @ApiModelProperty("合同状态")
    private String contractState;
    @ApiModelProperty("合同PDF地址")
    private String contractUrl;
    @ApiModelProperty("省份")
    private String provinceName;
    @ApiModelProperty("地区")
    private String areaName;
    @ApiModelProperty("城市")
    private String cityName;
    @ApiModelProperty("分公司")
    private String branchCompanyName;
    @ApiModelProperty("城市分站名称")
    private String cityBranchName;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("公司id")
    private String companyId;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getChildNo() {
        return childNo;
    }

    public void setChildNo(String childNo) {
        this.childNo = childNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getContractState() {
        return contractState;
    }

    public void setContractState(String contractState) {
        this.contractState = contractState;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public String getBranchCompanyName() {
        return branchCompanyName;
    }

    public void setBranchCompanyName(String branchCompanyName) {
        this.branchCompanyName = branchCompanyName;
    }

    public String getCityBranchName() {
        return cityBranchName;
    }

    public void setCityBranchName(String cityBranchName) {
        this.cityBranchName = cityBranchName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
