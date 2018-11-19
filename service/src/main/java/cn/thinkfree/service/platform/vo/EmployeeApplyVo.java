package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 *
 */
public class EmployeeApplyVo {
    @ApiModelProperty("申请人姓名")
    private String realName;
    @ApiModelProperty("证件号码")
    private String cardNo;
    @ApiModelProperty("申请时间")
    private long applyTime;
    @ApiModelProperty("处理时间")
    private long dealTime;
    @ApiModelProperty("处理状态，1已处理，2未处理，3已过期")
    private int dealState;
    @ApiModelProperty("处理人名称")
    private String dealUserName;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("实名认证状态，1未认证，2已认证，3实名认证审核中，4审核不通过")
    private int authState;
    @ApiModelProperty("员工手机号")
    private String phone;
    @ApiModelProperty("证件照1")
    private String certificatePhotoUrl1;
    @ApiModelProperty("证件照2")
    private String certificatePhotoUrl2;
    @ApiModelProperty("证件照3")
    private String certificatePhotoUrl3;
    @ApiModelProperty("员工申请状态，1入驻待审核，2入驻不通过，3已入驻，4解约待审核，5解约不通过，6已解约")
    private Integer employeeApplyState;

    public Integer getEmployeeApplyState() {
        return employeeApplyState;
    }

    public void setEmployeeApplyState(Integer employeeApplyState) {
        this.employeeApplyState = employeeApplyState;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public int getDealState() {
        return dealState;
    }

    public void setDealState(int dealState) {
        this.dealState = dealState;
    }

    public String getDealUserName() {
        return dealUserName;
    }

    public void setDealUserName(String dealUserName) {
        this.dealUserName = dealUserName;
    }

    public void setAuthState(int authState) {
        this.authState = authState;
    }

    public int getAuthState() {
        return authState;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
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
}
