package cn.tonghua.core.security.filter;

import cn.tonghua.core.security.factory.AuthenticationFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyCustomProcessFilter extends AbstractAuthenticationProcessingFilter {




    public  MyCustomProcessFilter(){
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    protected MyCustomProcessFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    protected MyCustomProcessFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException{

        Authentication token = new AuthenticationFactory().builder(httpServletRequest);

        Authentication authenticate = this.getAuthenticationManager().authenticate(token);
        if(authenticate != null && authenticate.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }
        return authenticate;
    }

}
