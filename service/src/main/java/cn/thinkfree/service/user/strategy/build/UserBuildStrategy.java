package cn.thinkfree.service.user.strategy.build;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserBuildStrategy {

    UserDetails build(String userID);

}
