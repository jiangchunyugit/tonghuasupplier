package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcUserInfo;

import java.util.Date;

public class PcUserInfoVo extends PcUserInfo {
    private Date lastLogin;
    private String password;
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
