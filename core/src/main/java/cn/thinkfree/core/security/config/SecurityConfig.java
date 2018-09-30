package cn.thinkfree.core.security.config;


import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.security.dao.SecurityResourceDao;
import cn.thinkfree.core.security.dao.SecurityUserDao;
import cn.thinkfree.core.security.exception.MySecurityException;
import cn.thinkfree.core.security.filter.*;
import cn.thinkfree.core.security.filter.util.SecurityConstants;

import cn.thinkfree.core.security.provider.MyCustomProvider;
import cn.thinkfree.core.security.utils.MultipleMd5;
import cn.thinkfree.core.utils.LogUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.event.LoggerListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * JAVA CONFIG SECURITY
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    private static final MyLogger logger = LogUtil.getLogger(SecurityConfig.class);

    @Autowired
    SecurityUserDao userService;
    @Autowired
    SecurityResourceDao securityResourceDao;

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    SecuritySuccessAuthHandler securitySuccessAuthHandler;

    @Override
    public void configure(WebSecurity web){
        // 设置不拦截规则
        web.ignoring()
                .antMatchers("/open/**")
                .antMatchers("/error")
                .antMatchers("/static/**", SecurityConstants.LOGIN_PAGE,"/**/*.jsp")
                .antMatchers("/api-docs", "/swagger-resources/**", "/swagger-ui.html","/webjars/**");

    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 设置拦截规则
        // 自定义accessDecisionManager访问控制器,并开启表达式语言
        http.authorizeRequests()
                .antMatchers("/role/**").hasRole("USER")
//                .anyRequest()
//                .permitAll()
//                .accessDecisionManager(accessDecisionManager())
//                .expressionHandler(webSecurityExpressionHandler())
//                .antMatchers("/**").access("hasRole('ROLE_USER')")
//                .antMatchers("/system/**").hasRole("ADMIN")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new MySecurityException());

        // 该死的Frame
        http.headers().frameOptions().disable();
        // 细粒度 更精细的配置~
//        http.addFilterAt(mySecurityPcFilter(), FilterSecurityInterceptor.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        MyCustomProcessFilter myCustomProcessFilter = new MyCustomProcessFilter();
        myCustomProcessFilter.setAuthenticationManager(authenticationManager());
        myCustomProcessFilter.setAuthenticationSuccessHandler(securitySuccessAuthHandler);
        myCustomProcessFilter.setAuthenticationFailureHandler(failHandler());
        http.addFilterAt(myCustomProcessFilter,UsernamePasswordAuthenticationFilter.class);
        // 自定义登录页面
        http.csrf().disable()
                .formLogin()
                .loginPage(SecurityConstants.LOGIN_PAGE)
//                .successHandler(successHandler())
                .successHandler(securitySuccessAuthHandler)
                .failureHandler(failHandler())
                .loginProcessingUrl(SecurityConstants.LOGIN_URL)
                .usernameParameter(SecurityConstants.LOGIN_USERNAME)
                .passwordParameter(SecurityConstants.LOGIN_PASSWORD)
                .permitAll();

        // 自定义注销
        http.logout()
                .logoutUrl(SecurityConstants.LOGIN_OUT)
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setCharacterEncoding("UTF-8");
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
                        MyRespBundle<String> resp = new MyRespBundle<>();
                        resp.setTimestamp(Instant.now().toEpochMilli());
                        resp.setMessage("操作成功!");
                        resp.setCode(200);
                        resp.setData("操作成功");
                        httpServletResponse.getWriter().write(new Gson().toJson(resp));
                    }
                })
//                .logoutSuccessUrl(SecurityConstants.LOGIN_PAGE)
                .invalidateHttpSession(true);

        // session管理
//        http.sessionManagement().sessionFixation().changeSessionId()
//                .maximumSessions(1).expiredUrl("/login");
        // RemeberMe
        http.rememberMe().key(SecurityConstants.REMEMBERME_KEY);
    }

    /**
     * 自定义安全链过滤器
     * @return
     */
    @Bean
    public Filter mySecurityPcFilter() {
        SecurityPcFilter securityPcFilter = new SecurityPcFilter();
        securityPcFilter.setAccessDecisionManager(accessDecisionManager());
        securityPcFilter.setSecurityMetadataSource(resourceHandler());
        return securityPcFilter;
    }


    /**
     * 自定义资源加载器
     * @return
     */
    @Bean
    public FilterInvocationSecurityMetadataSource resourceHandler() {
        SecurityInvocationSecurityMetadataSource resourceHandler = new SecurityInvocationSecurityMetadataSource(securityResourceDao);
        resourceHandler.setRejectPublicInvocations(true);
        resourceHandler.setHotDeployment(true);
        return resourceHandler;
    }

    /**
     *  配置鉴权失败处理器
     * @return
     */
    protected AuthenticationFailureHandler failHandler() {
        SecurityFailAuthHandler  securityFailAuthHandler = new SecurityFailAuthHandler();
        return securityFailAuthHandler;
    }

//    /**
//     * 配置鉴权成功处理器
//     * @return
//     */
//    protected AuthenticationSuccessHandler successHandler(){
//        SecuritySuccessAuthHandler securitySuccessAuthHandler = new SecuritySuccessAuthHandler();
//        securitySuccessAuthHandler.setDefaultTargetUrl(SecurityConstants.LOGIN_SUCCESS_PAGE);
//        return securitySuccessAuthHandler;
//    }




    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(new MyCustomProvider(userService,new MultipleMd5()));
        auth.userDetailsService(userService).passwordEncoder(new MultipleMd5());
    }


    @Bean
    public LoggerListener loggerListener() {
        logger.info("配置默认时间监听器");
        LoggerListener loggerListener = new LoggerListener();
        return loggerListener;
    }

    @Bean
    public LoggerListener eventLoggerListener() {
        logger.info("配置全局时间监听器");
        LoggerListener eventLoggerListener = new  LoggerListener();
        return eventLoggerListener;
    }


    /**
     *
     * 这里可以增加自定义的投票器
     */
    @Bean(name = "accessDecisionManager")
    public AccessDecisionManager accessDecisionManager() {
        logger.info("配置鉴权器");
        return new SecurityAccessDecisionManager();

    }


}
