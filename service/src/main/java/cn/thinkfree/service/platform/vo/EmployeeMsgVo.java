package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 员工信息
 */
public class EmployeeMsgVo {

    @ApiModelProperty("所属公司名称")
    private String companyName;
    @ApiModelProperty("用户真实姓名")
    private String realName;
    @ApiModelProperty("用户所属角色名称")
    private String roleName;
    @ApiModelProperty("用户所属角色编码")
    private String roleCode;
    @ApiModelProperty("员工申请状态，1未绑定，2已绑定，3审核中，4审核不通过")
    private int bindCompanyState;
    @ApiModelProperty("实名认证状态，1未认证，2已认证，3实名认证审核中，4审核不通过")
    private int authState;
    @ApiModelProperty("用户手机号")
    private String phone;
    @ApiModelProperty("用户头像地址")
    private String iconUrl;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("证件号码")
    private String certificate;
    @ApiModelProperty("证件照片1")
    private String certificatePhotoUrl1;
    @ApiModelProperty("证件照片2")
    private String certificatePhotoUrl2;
    @ApiModelProperty("证件照片3")
    private String certificatePhotoUrl3;
    @ApiModelProperty("证件类型编码")
    private String certificateType;
    @ApiModelProperty("证件类型名称")
    private String certificateTypeName;
    @ApiModelProperty("国家类型编码")
    private String countryCode;
    @ApiModelProperty("国家类型名称")
    private String countryCodeName;
    @ApiModelProperty("性别，1男，2女")
    private String sex;
    @ApiModelProperty("邮箱地址")
    private String email;
    @ApiModelProperty("所在地")
    private String address;
    @ApiModelProperty("工作年限")
    private String workTime;
    @ApiModelProperty("注册时间")
    private String registerTime;

    public void setAuthState(int authState) {
        this.authState = authState;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public int getBindCompanyState() {
        return bindCompanyState;
    }

    public void setBindCompanyState(int bindCompanyState) {
        this.bindCompanyState = bindCompanyState;
    }

    public int getAuthState() {
        return authState;
    }

    public void setAuthState(Integer authState) {
        if (authState != null){
            this.authState = authState;
        }else {
            authState = 1;
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificatePhotoUrl1() {
        return certificatePhotoUrl1;
    }

    public void setCertificatePhotoUrl1(String certificatePhotoUrl1) {
        this.certificatePhotoUrl1 = certificatePhotoUrl1;
    }

    public String getCertificatePhotoUrl2() {
        return certificatePhotoUrl2;
    }

    public void setCertificatePhotoUrl2(String certificatePhotoUrl2) {
        this.certificatePhotoUrl2 = certificatePhotoUrl2;
    }

    public String getCertificatePhotoUrl3() {
        return certificatePhotoUrl3;
    }

    public void setCertificatePhotoUrl3(String certificatePhotoUrl3) {
        this.certificatePhotoUrl3 = certificatePhotoUrl3;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateTypeName() {
        return certificateTypeName;
    }

    public void setCertificateTypeName(String certificateTypeName) {
        this.certificateTypeName = certificateTypeName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCodeName() {
        return countryCodeName;
    }

    public void setCountryCodeName(String countryCodeName) {
        this.countryCodeName = countryCodeName;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterTime() {
        return registerTime;
    }
}
