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

    public UserMsgVo() {
    }

    public UserMsgVo(String userId, String userName, String userPhone, String userType, String userIcon) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userType = userType;
        this.userIcon = userIcon;
    }

    public UserMsgVo(String userId, String userName, String userPhone, String userType, String realName, String userIcon) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userType = userType;
        this.realName = realName;
        this.userIcon = userIcon;
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
}
