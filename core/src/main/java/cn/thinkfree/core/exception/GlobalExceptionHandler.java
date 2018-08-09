package cn.thinkfree.core.exception;


import cn.thinkfree.core.logger.AbsLogPrinter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 全局异常拦截器
 */
@ControllerAdvice
public class GlobalExceptionHandler extends AbsLogPrinter {
	

	@ExceptionHandler(value = BadRequestException.class)
    @ResponseBody
    public ErrorInfo<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, BadRequestException e) throws Exception {
		responseHandler(response, HttpStatus.BAD_REQUEST);
		return buildErrorInfo(HttpStatus.BAD_REQUEST, e, req);
    }
	

	@ExceptionHandler(value = InternalServerException.class)
    @ResponseBody
    public ErrorInfo<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, InternalServerException e) throws Exception {
        responseHandler(response, HttpStatus.INTERNAL_SERVER_ERROR);
		return buildErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, e, req);
    }
	

	@ExceptionHandler(value = ForbiddenException.class)
    @ResponseBody
    public ErrorInfo<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, ForbiddenException e) throws Exception {
        responseHandler(response, HttpStatus.FORBIDDEN);
		return buildErrorInfo(HttpStatus.FORBIDDEN, e, req);
    }

	@ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public ErrorInfo<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, UnauthorizedException e) throws Exception {
        responseHandler(response, HttpStatus.UNAUTHORIZED);
		return buildErrorInfo(HttpStatus.UNAUTHORIZED, e, req);
    }

	

	@ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        responseHandler(response, HttpStatus.INTERNAL_SERVER_ERROR);
		printErrorMes(e.getMessage());
		e.printStackTrace();
		return buildErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, "内部服务异常", req);
    }

	private void responseHandler(HttpServletResponse response, HttpStatus status){
		if(response != null)
			response.setStatus(status.value());
	}

	private ErrorInfo<String> buildErrorInfo(HttpStatus code, Exception e, HttpServletRequest req){
		printErrorMes(e.getMessage());
		e.printStackTrace();
		return buildErrorInfo(code, e.getMessage(), req);
	}

	private ErrorInfo<String> buildErrorInfo(HttpStatus code, String message, HttpServletRequest req){
		ErrorInfo<String> info = new ErrorInfo<>(code.value(),message);
		if(req != null){
			info.setUrl(req.getRequestURL().toString());
		}
		return info;
	}
	

}
