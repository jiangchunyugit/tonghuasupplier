package cn.thinkfree.core.security.dao;

import cn.thinkfree.core.security.token.MyCustomUserDetailToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by lenovo on 2017/2/24.
 */
public interface SecurityUserDao extends UserDetailsService {

    UserDetails loadPlatformUser(MyCustomUserDetailToken.TokenDetail detail);
}
