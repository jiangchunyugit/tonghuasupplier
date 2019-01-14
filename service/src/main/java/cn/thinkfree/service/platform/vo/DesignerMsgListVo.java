package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 设计师信息VO
 */
public class DesignerMsgListVo {
    @ApiModelProperty("设计师真实姓名")
    private String userName;
    @ApiModelProperty("设计师用户名")
    private String realName;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("所在地")
    private String address;
    @ApiModelProperty("设计师身份")
    private String identityName;
    @ApiModelProperty("来源")
    private String source;
    @ApiModelProperty("注册时间")
    private String registrationTime;
    @ApiModelProperty("设计师标签")
    private String tag;
    @ApiModelProperty("实名认证状态")
    private String authState;
    @ApiModelProperty("归属设计公司")
    private String companyName;
    @ApiModelProperty("设计师等级")
    private String levelName;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("设计师绑定公司状态  1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约")
    private String bindCompanyState;
    @ApiModelProperty("分公司")
    private String branchCompanyName;
    @ApiModelProperty("城市分站名称")
    private String cityBranchName;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("公司id")
    private String companyId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAuthState() {
        return authState;
    }

    public void setAuthState(String authState) {
        this.authState = authState;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBindCompanyState(String bindCompanyState) {
        this.bindCompanyState = bindCompanyState;
    }

    public String getBindCompanyState() {
        return bindCompanyState;
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
