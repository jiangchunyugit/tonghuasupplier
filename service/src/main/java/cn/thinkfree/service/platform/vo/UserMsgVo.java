package cn.thinkfree.service.platform.vo;

/**
 * @author xusonghui
 * 用户信息对象
 */
public class UserMsgVo {

    private String userId;

    private String userName;

    private String userPhone;

    private String userType;

    private String realName;

    private String userIcon;

    private String staffId;

    private String consumerId;

    private String registerTime;

    private String memberEcode;

    public UserMsgVo() {
    }

    public UserMsgVo(String userId, String userName, String userPhone, String userType, String realName, String userIcon) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userType = userType;
        this.realName = realName;
        this.userIcon = userIcon;
        this.consumerId = userId;
    }

    public UserMsgVo(String userId, String userName, String userPhone, String userType, String realName, String userIcon, String memberEcode) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userType = userType;
        this.realName = realName;
        this.userIcon = userIcon;
        this.memberEcode = memberEcode;
        this.consumerId = userId;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setMemberEcode(String memberEcode) {
        this.memberEcode = memberEcode;
    }

    public String getMemberEcode() {
        return memberEcode;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterTime() {
        return registerTime;
    }
}
