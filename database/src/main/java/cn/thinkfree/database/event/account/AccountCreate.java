package cn.thinkfree.database.event;

import cn.thinkfree.core.event.AbsBaseEvent;


public class AccountCreate extends AbsBaseEvent {

    private String source;

    /**
     * 邮箱或者手机号
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    public AccountCreate(){}

    public AccountCreate(String userCode){
        super();
        this.source = userCode;
    }

    public AccountCreate(String userCode, String phone, String password) {
        super();
        this.source = userCode;
        this.email = phone;
        this.password = password;
    }

    /**
     * 获取 事件源
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
