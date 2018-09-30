package cn.thinkfree.service.user.strategy.build;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SmartUserBuildStrategy implements UserBuildStrategy {


    @Override
    public UserDetails build(String userID) {

        return null;
    }
}
