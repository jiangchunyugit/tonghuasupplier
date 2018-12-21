package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.event.model.UserLoginAfter;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.model.UserLoginLog;
import cn.thinkfree.service.user.UserService;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用户登录后
 */
@Component
public class UserLoginAfterListener   extends AbsLogPrinter {

    @Autowired
    UserService userService;

    @EventListener
    @Async
    public void userLoginAfter(UserLoginAfter userLoginAfter) {
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setIp(userLoginAfter.getIp());
        userLoginLog.setAppOs(Short.valueOf("1"));
        userLoginLog.setPhone(userLoginAfter.getPhone());
        userLoginLog.setLoginTime(new Date());
        userLoginLog.setUserId(userLoginAfter.getSource());
        userService.userLoginAfter(userLoginLog);
    }
}
