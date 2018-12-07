package cn.thinkfree.database.event.account;

import cn.thinkfree.core.event.BaseEvent;

/**
 * 忘记密码重置成功
 */
public class ForgetPwd implements BaseEvent {
    private String source;
    private String pwd;
    public ForgetPwd(String email) {
        this.source = email;
    }




    @Override
    public String getSource() {
        return source;
    }

    public ForgetPwd(String email, String pwd) {
        this.source = email;
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }
}
