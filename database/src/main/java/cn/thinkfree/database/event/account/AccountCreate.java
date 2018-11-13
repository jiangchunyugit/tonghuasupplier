package cn.thinkfree.database.event.account;

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

    /**
     * 用户名称
     */
    private String userName;

    private Boolean isPhone;

    public AccountCreate(String userCode, String phone, String password, String name){
        super();
        this.source = userCode;
        this.email = phone;
        this.password = password;
        this.userName = name;
        this.isPhone = false;
    }
    public AccountCreate(String userCode, String phone, String password, String name,Boolean isPhone){
        super();
        this.source = userCode;
        this.email = phone;
        this.password = password;
        this.userName = name;
        this.isPhone = isPhone;
    }

    public AccountCreate(String userCode){
        super();
        this.source = userCode;
        this.isPhone = false;
    }

    public AccountCreate(String userCode, String phone, String password) {
        super();
        this.source = userCode;
        this.email = phone;
        this.password = password;
        this.isPhone = false;
    }

    /**
     * 获取 事件源
     *
     * @return
     */
    public String getSource() {
        return source;
    }

    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }

    public String getUserName() {
        return userName;
    }
    public Boolean isPhone(){
        return isPhone;
    }
}
