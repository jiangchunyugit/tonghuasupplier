package cn.thinkfree.service.user.strategy.build;

import cn.thinkfree.core.security.model.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserBuildStrategy {

    SecurityUser build(String userID);

}
