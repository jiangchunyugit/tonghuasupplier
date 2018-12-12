package cn.thinkfree.core.security.provider;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.security.dao.SecurityUserDao;
import cn.thinkfree.core.security.token.MyCustomUserDetailToken;
import cn.thinkfree.core.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public abstract class AbsMyCustomProvider implements AuthenticationProvider, InitializingBean {
     protected MyLogger logger = LogUtil.getLogger(this.getClass());
     protected PasswordEncoder passwordEncoder;
     protected SecurityUserDao userService;
     protected UserDetailsChecker userDetailsChecker = user -> {
         if (!user.isAccountNonLocked()) {
             logger.debug("User account is locked");
             throw new LockedException("账户锁定");
         } else if (!user.isEnabled()) {
              logger.debug("User account is disabled");
             throw new DisabledException("账户禁用");
         } else if (!user.isAccountNonExpired()) {
              logger.debug("User account is expired");
             throw new AccountExpiredException("账户过期");
         }else if (!user.isCredentialsNonExpired()) {
              logger.debug("User account credentials have expired");
             throw new CredentialsExpiredException("用户凭证过期");
         }
     };

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(MyCustomUserDetailToken.class, authentication, "不支持的类型");

        UserDetails user = null;
        try {
            user = makeUserDetail(authentication);
            if(isNeedChecks()){
                userDetailsChecker.check(user);
            }
            checkCredentials(authentication.getCredentials(),user.getPassword());

            System.out.println(authentication);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("无此用户");
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("坏的鉴权");
        }
        Assert.notNull(user,"用户信息异常");
        return convertUserDetail2Principal(authentication,user);
    }

    protected  void checkCredentials(Object credentials, String password){
        if(StringUtils.isBlank(password) || StringUtils.isBlank(credentials.toString())){
            throw new BadCredentialsException("密码为空");
        }
        if(!passwordEncoder.encode(credentials.toString()).equals(password)){
            throw new BadCredentialsException("密码错误");

        }
    };

    protected abstract boolean isNeedChecks();

    protected abstract UserDetails makeUserDetail(Authentication authentication) throws UsernameNotFoundException ;

    protected Authentication convertUserDetail2Principal( Authentication authentication, UserDetails user){
        MyCustomUserDetailToken token = new MyCustomUserDetailToken(user,authentication.getCredentials(),user.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    };

    @Override
    public void afterPropertiesSet() throws Exception {
        this.afterPropertiesSet0();
    }

    protected void afterPropertiesSet0() throws Exception{
        logger.debug("初始化后执行");
    }
}
