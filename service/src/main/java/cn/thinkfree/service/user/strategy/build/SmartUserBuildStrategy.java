package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.model.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 啥都不干
 */
@Component
public class SmartUserBuildStrategy extends AbsLogPrinter implements UserBuildStrategy {


    @Override
    public SecurityUser build(String userID) {
        printInfoMes("Yes, I know, but I am not involved.");
        return null;
    }
}
