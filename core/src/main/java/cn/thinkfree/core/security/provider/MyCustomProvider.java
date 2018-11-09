package cn.thinkfree.core.security.provider;

import cn.thinkfree.core.security.dao.SecurityUserDao;
import cn.thinkfree.core.security.token.MyCustomUserDetailToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义权限处理器
 * support支持类型会走authenticate校验
 * 总是有神奇的需求需要处理
 */
public class MyCustomProvider extends AbsMyCustomProvider {



    public MyCustomProvider(){}

    public MyCustomProvider(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
    public MyCustomProvider(SecurityUserDao userService,PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected UserDetails makeUserDetail(Authentication authentication) throws UsernameNotFoundException {
        MyCustomUserDetailToken.TokenDetail detail = (MyCustomUserDetailToken.TokenDetail) authentication.getDetails();
        UserDetails user = userService.loadPlatformUser(detail);
        if(user == null){
            throw new UsernameNotFoundException("无此用户");
        }
        return user;
    }

    /**
     * spring 特性 先判断后处理
     * @param kls
     * @return
     */
    @Override
    public boolean supports(Class<?> kls) {
        return  MyCustomUserDetailToken.class.isAssignableFrom(kls);
    }

    @Override
    protected boolean isNeedChecks() {
        return false;
    }


}
