package cn.thinkfree.core.security.filter;

import cn.thinkfree.core.bundle.MyRespBundle;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 *
 */
public class SecurityFailAuthHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        super.onAuthenticationFailure(request, response, exception);
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charset=utf-8");
        exception.printStackTrace();

        MyRespBundle<String> result = new MyRespBundle<>();
        result.setData("登录失败!");
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setMessage("登录失败!");
        result.setTimestamp(Instant.now().toEpochMilli());

        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Content-Type","application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(new Gson().toJson(result));
    }
}
