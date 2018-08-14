package cn.thinkfree.core.security.config;


import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.security.exception.MySecurityException;
import cn.thinkfree.core.security.filter.SercuityFailAuthHandler;
import cn.thinkfree.core.security.filter.SercuitySuccessAuthHandler;
import cn.thinkfree.core.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.event.LoggerListener;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * JAVA CONFIG SECURITY
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    private static final MyLogger logger = LogUtil.getLogger(SecurityConfig.class);




    /**
     * 全局配置
     * @param auth 配置授权上下文
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

//        auth.inMemoryAuthentication()
//            .withUser("user").password("password").roles("USER");

    }
    //    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userService);
////        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置不拦截规则
        web.ignoring()
                .anyRequest()
                .antMatchers("/static/**")
                .antMatchers("/**/*.jsp")
//                .antMatchers("/login")
                .antMatchers("/login");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 设置拦截规则
        // 自定义accessDecisionManager访问控制器,并开启表达式语言
        http.authorizeRequests()
//                .accessDecisionManager(accessDecisionManager())
//                .expressionHandler(webSecurityExpressionHandler())
                .antMatchers("/role/**").hasRole("USER")
//                .antMatchers("/system/**").hasRole("ADMIN")
                .and()
                .exceptionHandling()
//                .accessDeniedPage("/login")
                .authenticationEntryPoint(new MySecurityException());

        // 该死的Frame
        http.headers().frameOptions().disable();

        // 自定义登录页面
        http.csrf().disable()
                .formLogin().loginPage("/login")
                .successHandler(successHandler())
                .failureHandler(failHandler())

                .loginProcessingUrl("/j_spring_security_check")
                .usernameParameter("j_username")
                .passwordParameter("j_password").permitAll();

        // 自定义注销
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .invalidateHttpSession(true);

        // session管理
        http.sessionManagement().sessionFixation().changeSessionId()
                .maximumSessions(1).expiredUrl("/login");
        // RemeberMe
//        http.rememberMe().key("webmvc#FD637E6D9C0F1A5A67082AF56CE32485");

    }

    /**
     *  配置鉴权失败处理器
     * @return
     */
    protected AuthenticationFailureHandler failHandler() {
        SercuityFailAuthHandler sercuityFailAuthHandler=new SercuityFailAuthHandler();
        return sercuityFailAuthHandler;
    }

    /**
     * 配置鉴权成功处理器
     * @return
     */
    protected AuthenticationSuccessHandler successHandler(){
        SercuitySuccessAuthHandler sercuitySuccessAuthHandler=new SercuitySuccessAuthHandler();
        sercuitySuccessAuthHandler.setDefaultTargetUrl("/index");
        return sercuitySuccessAuthHandler;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        // 自定义UserDetailsService
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(new BCryptPasswordEncoder() );

    }

//    @Bean(name="userService")
//    public UserDetailsService userDetailsService() {
//        logger.info("配置用户登录器");
////        return new UserServiceImpl();
//        return  null;
//    }

    @Bean
    public LoggerListener loggerListener() {
        logger.info("配置默认时间监听器");
        LoggerListener loggerListener = new LoggerListener();

        return loggerListener;
    }

    @Bean
    public org.springframework.security.access.event.LoggerListener eventLoggerListener() {
        logger.info("配置全局时间监听器");
        org.springframework.security.access.event.LoggerListener eventLoggerListener =
                new  org.springframework.security.access.event.LoggerListener();

        return eventLoggerListener;
    }

//    /*
//     *
//     * 这里可以增加自定义的投票器
//     */
//    @Bean(name = "accessDecisionManager")
//    public AccessDecisionManager accessDecisionManager() {
//        logger.info("配置鉴权器");
//        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
//        decisionVoters.add(new RoleVoter());
//        decisionVoters.add(new AuthenticatedVoter());
//        decisionVoters.add(webExpressionVoter());// 启用表达式投票器
//        AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);
////        new SercuityAccessDecisionManager()
//        return accessDecisionManager;
//    }


//    /*
//     * 表达式控制器
//     */
//    @Bean(name = "expressionHandler")
//    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
//        logger.info("配置表达式处理器");
//        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
//        return webSecurityExpressionHandler;
//    }
//
//    /*
//     * 表达式投票器
//     */
//    @Bean(name = "expressionVoter")
//    public WebExpressionVoter webExpressionVoter() {
//        logger.info("配置投票器");
//        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
//        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
//        return webExpressionVoter;
//    }

}
