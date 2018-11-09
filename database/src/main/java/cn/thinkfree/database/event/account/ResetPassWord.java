package cn.thinkfree.database.event.account;

import cn.thinkfree.core.event.AbsBaseEvent;

/**
 * 重置密码事件
 */
public class ResetPassWord extends AbsBaseEvent {

    /**
     * 事件源
     */
    private String source;

    /**
     * 发送地址
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    public ResetPassWord(){}
    public ResetPassWord(String userID, String phone, String password) {
        super();
        this.source = userID;
        this.email = phone;
        this.password = password;
    }

    /**
     * 获取事件源
     *
     * @return
     */
    @Override
    public String getSource() {
        return source;
    }

    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }

}
