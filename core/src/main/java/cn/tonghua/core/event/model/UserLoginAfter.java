package cn.tonghua.core.event.model;


import cn.tonghua.core.event.AbsBaseEvent;

public class UserLoginAfter extends AbsBaseEvent {

    private String source;

    /**
     * 登录IP
     */
    private String ip;
    private String phone;


    @Override
    public String getSource() {
        return this.source;
    }
    public UserLoginAfter(String uid, String phone, String requestIp){
        this.source = uid;
        this.ip = requestIp;
        this.phone = phone;
    }

    public String getIp() {
        return ip;
    }

    public String getPhone() {
        return phone;
    }
}

