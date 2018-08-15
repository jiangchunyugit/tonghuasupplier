package cn.thinkfree.core.event.model;


import cn.thinkfree.core.model.AbsMyEvent;

public class UserLoginAfter extends AbsMyEvent {

    private String source;

    @Override
    public String getSource() {
        return this.source;
    }
    public UserLoginAfter(String uid){
        this.source = uid;
    }
}

