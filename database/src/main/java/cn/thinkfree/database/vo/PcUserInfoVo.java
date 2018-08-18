package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcUserInfo;

import java.util.Date;

public class PcUserInfoVo extends PcUserInfo {
    private Date lastLogin;

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
