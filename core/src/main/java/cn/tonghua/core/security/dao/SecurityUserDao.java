package cn.tonghua.core.security.dao;

import cn.tonghua.core.security.token.MyCustomUserDetailToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by lenovo on 2017/2/24.
 */
public interface SecurityUserDao extends UserDetailsService {

    UserDetails loadPlatformUser(MyCustomUserDetailToken.TokenDetail detail);
}
