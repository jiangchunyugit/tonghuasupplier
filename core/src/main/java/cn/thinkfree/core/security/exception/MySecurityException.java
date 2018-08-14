package cn.thinkfree.core.security.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.thinkfree.core.base.AbsBaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


public class MySecurityException extends AbsBaseController implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		String isApp=request.getHeader("isApp");
		if(StringUtils.isNotBlank(isApp)){
//			wrapDatas(response, true, null, "鉴权失败",null);
		}else{
			response.sendRedirect(request.getContextPath()+"/login?error=2");
 		}
//   		logger.error("鉴权失败:{}",authException);
 	}
 
}
