package cn.thinkfree.core.security.factory;

import cn.thinkfree.core.security.token.MyCustomUserDetailToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationFactory {



    public Authentication builder(HttpServletRequest httpServletRequest){

        String userName = obtainUserName(httpServletRequest);
        String passWord = obtainPassword(httpServletRequest);
        String type = httpServletRequest.getParameter("type");
        Authentication token ;
        if(StringUtils.isBlank(type)){
              token = new UsernamePasswordAuthenticationToken(userName,passWord);
        }else{
              token = new MyCustomUserDetailToken(userName,passWord,type);
        }
        if(token != null){
            Object details = new WebAuthenticationDetailsSource().buildDetails(httpServletRequest);
            ((AbstractAuthenticationToken) token).setDetails(details);
        }
        return token;
    }

    private String obtainPassword(HttpServletRequest httpServletRequest) {
        String passWord =httpServletRequest.getParameter("password");
        return StringUtils.isBlank(passWord) ? "" : passWord.trim();
    }

    private String obtainUserName(HttpServletRequest httpServletRequest) {
        String userName = httpServletRequest.getParameter("username");
        return StringUtils.isBlank(userName) ? "":userName.trim();
    }

}
