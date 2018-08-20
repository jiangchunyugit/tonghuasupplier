package cn.thinkfree.database.vo;

import cn.thinkfree.core.model.BaseModel;

public class StaffsVO extends BaseModel {

    /**
     * 用户主键
     */
    private String userID;
    /**
     * 手机号
     */
    private String phone;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
