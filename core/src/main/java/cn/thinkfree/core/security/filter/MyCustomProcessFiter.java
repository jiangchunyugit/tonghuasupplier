package cn.thinkfree.core.security.filter;

import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.core.security.token.MyCustomUserDetailToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyCustomProcessFiter extends UsernamePasswordAuthenticationFilter {


    public MyCustomProcessFiter(){

    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException{
        System.out.println("执行过程中");
        String userName = httpServletRequest.getParameter("userName");
        String passWord =  httpServletRequest.getParameter("passWord");
        String type = httpServletRequest.getParameter("type");
//        SecurityUser user = new Sec
        MyCustomUserDetailToken token = new MyCustomUserDetailToken(null,null);
//        setDetails(httpServletRequest,token);

        return this.getAuthenticationManager().authenticate(token);
    }
}
