package cn.thinkfree.database.event.account;

import cn.thinkfree.core.event.BaseEvent;

/**
 * 忘记密码重置成功
 */
public class ForgetPwd implements BaseEvent {
    private String source;
    public ForgetPwd(String email) {
        this.source = email;
    }

    @Override
    public String getSource() {
        return source;
    }
}
