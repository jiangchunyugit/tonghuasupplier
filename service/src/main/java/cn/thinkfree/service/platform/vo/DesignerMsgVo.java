package cn.thinkfree.service.platform.vo;

import cn.thinkfree.database.model.DesignerMsg;
import cn.thinkfree.database.model.DesignerStyleConfig;
import cn.thinkfree.database.model.EmployeeMsg;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author xusonghui
 * 设计师信息
 */
public class DesignerMsgVo {

    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("性别，1男，2女")
    private int sex;
    @ApiModelProperty("出生年月")
    private String birthday;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("所在地")
    private String address;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("证件号")
    private String certificate;
    @ApiModelProperty("证件正面")
    private String certificateUrl1;
    @ApiModelProperty("证件反面")
    private String certificateUrl2;
    @ApiModelProperty("手持证件照")
    private String certificateUrl3;
    @ApiModelProperty("实名认证状态，1未认证，2已认证")
    private int authState;
    @ApiModelProperty("来源，1无来源，2用户注册，3后台创建")
    private int source;
    @ApiModelProperty("注册时间")
    private String registerTime;
    @ApiModelProperty("设计师标签")
    private String designTag;
    @ApiModelProperty("设计师等级")
    private int level;
    @ApiModelProperty("设计师身份,1社会化设计师")
    private String identity;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("从业年限")
    private int workingTime;
    @ApiModelProperty("量房费")
    private String volumeRoomMoney;
    @ApiModelProperty("设计费最低")
    private String designerMoneyLow;
    @ApiModelProperty("设计费最高")
    private String designerMoneyHigh;
    @ApiModelProperty("擅长风格")
    private List<String> designerStyles;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificateUrl1() {
        return certificateUrl1;
    }

    public void setCertificateUrl1(String certificateUrl1) {
        this.certificateUrl1 = certificateUrl1;
    }

    public String getCertificateUrl2() {
        return certificateUrl2;
    }

    public void setCertificateUrl2(String certificateUrl2) {
        this.certificateUrl2 = certificateUrl2;
    }

    public String getCertificateUrl3() {
        return certificateUrl3;
    }

    public void setCertificateUrl3(String certificateUrl3) {
        this.certificateUrl3 = certificateUrl3;
    }

    public int getAuthState() {
        return authState;
    }

    public void setAuthState(int authState) {
        this.authState = authState;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getDesignTag() {
        return designTag;
    }

    public void setDesignTag(String designTag) {
        this.designTag = designTag;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }

    public String getVolumeRoomMoney() {
        return volumeRoomMoney;
    }

    public void setVolumeRoomMoney(String volumeRoomMoney) {
        this.volumeRoomMoney = volumeRoomMoney;
    }

    public String getDesignerMoneyLow() {
        return designerMoneyLow;
    }

    public void setDesignerMoneyLow(String designerMoneyLow) {
        this.designerMoneyLow = designerMoneyLow;
    }

    public String getDesignerMoneyHigh() {
        return designerMoneyHigh;
    }

    public void setDesignerMoneyHigh(String designerMoneyHigh) {
        this.designerMoneyHigh = designerMoneyHigh;
    }

    public List<String> getDesignerStyles() {
        return designerStyles;
    }

    public void setDesignerStyles(List<String> designerStyles) {
        this.designerStyles = designerStyles;
    }
}
