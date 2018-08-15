package cn.thinkfree.service.event;

import cn.thinkfree.core.event.MyEventBus;
import cn.thinkfree.core.event.model.UserLoginAfter;
import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MyEventListener {


    @Subscribe
    public void userLoginAfter(UserLoginAfter userLoginAfter){
        System.out.println("这里应该是记录登录日志");
    }

    @PostConstruct
    public void init(){
        MyEventBus.getInstance().register(this);
    }


}
