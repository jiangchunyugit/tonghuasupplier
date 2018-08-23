package cn.thinkfree.service.event;

import cn.thinkfree.core.event.MyEventBus;
import cn.thinkfree.core.event.model.UserLoginAfter;
import cn.thinkfree.database.mapper.UserLoginLogMapper;
import cn.thinkfree.database.model.UserLoginLog;
import cn.thinkfree.service.user.UserService;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class MyEventListener {

    @Autowired
    UserService userService;


    @Subscribe
    public void userLoginAfter(UserLoginAfter userLoginAfter) {
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setIp(userLoginAfter.getIp());
        userLoginLog.setAppOs(Short.valueOf("1"));
        userLoginLog.setPhone(userLoginAfter.getPhone());
        userLoginLog.setLoginTime(new Date());
        userLoginLog.setUserId(userLoginAfter.getSource());
        userService.userLoginAfter(userLoginLog);
    }

    @PostConstruct
    public void init(){
        MyEventBus.getInstance().register(this);
    }


}
