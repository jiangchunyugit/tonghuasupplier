package cn.thinkfree.core.exception;


import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.utils.VersionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.time.Instant;


/**
 * 全局异常拦截器
 */
@ControllerAdvice
public class GlobalExceptionHandler extends AbsLogPrinter {
	

	@ExceptionHandler(value = BadRequestException.class)
    @ResponseBody
    public MyRespBundle<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, BadRequestException e) throws Exception {
		responseHandler(response, HttpStatus.BAD_REQUEST);
		return buildErrorInfo(HttpStatus.BAD_REQUEST, e, req);
    }

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	@ResponseBody
	public MyRespBundle<String> exceptionBadRequestHandler(HttpServletRequest req, HttpServletResponse response, MissingServletRequestParameterException e) throws Exception {
		responseHandler(response, HttpStatus.BAD_REQUEST);
		return buildErrorInfo(HttpStatus.BAD_REQUEST, e, req);
	}

	@ExceptionHandler(value = InternalServerException.class)
    @ResponseBody
    public MyRespBundle<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, InternalServerException e) throws Exception {
        responseHandler(response, HttpStatus.INTERNAL_SERVER_ERROR);
		return buildErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, e, req);
    }
	

	@ExceptionHandler(value = ForbiddenException.class)
    @ResponseBody
    public MyRespBundle<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, ForbiddenException e) throws Exception {
        responseHandler(response, HttpStatus.FORBIDDEN);
		return buildErrorInfo(HttpStatus.FORBIDDEN, e, req);
    }

	@ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public MyRespBundle<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, UnauthorizedException e) throws Exception {
        responseHandler(response, HttpStatus.UNAUTHORIZED);
		return buildErrorInfo(HttpStatus.UNAUTHORIZED, e, req);
    }

    @ExceptionHandler(value = ValidationException.class)
	@ResponseBody
	public MyRespBundle<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, ValidationException e) throws Exception {
		responseHandler(response, HttpStatus.BAD_REQUEST);
		return buildErrorInfo(HttpStatus.BAD_REQUEST, e, req);
	}
	@ExceptionHandler(value = BindException.class)
	@ResponseBody
	public MyRespBundle<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, BindException e) throws Exception {
		System.out.println(e);
		responseHandler(response, HttpStatus.BAD_REQUEST);
		return buildErrorInfo(HttpStatus.BAD_REQUEST, e, req);
	}


	@ExceptionHandler(value = Exception.class)
    @ResponseBody
    public MyRespBundle<String> exceptionHandler(HttpServletRequest req, HttpServletResponse response, Exception e) throws Exception {
        responseHandler(response, HttpStatus.INTERNAL_SERVER_ERROR);
		printErrorMes(e.getMessage());
		e.printStackTrace();
		return buildErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, "内部服务异常", req);
    }

	private void responseHandler(HttpServletResponse response, HttpStatus status){
		if(response != null)
			response.setStatus(status.value());
	}

	private MyRespBundle<String> buildErrorInfo(HttpStatus code, ValidationException e, HttpServletRequest req){
		printErrorMes(e.getMessage());
		e.printStackTrace();
		MyRespBundle<String> resp = buildErrorInfo(code, e.getMessage(), req);
		resp.setData(e.getMessage());
		resp.setMessage("参数错误");
		return  resp;
	}



	private MyRespBundle<String> buildErrorInfo(HttpStatus code, Exception e, HttpServletRequest req){
		printErrorMes(e.getMessage());
		e.printStackTrace();
		return buildErrorInfo(code, e.getMessage(), req);
	}

	private MyRespBundle<String> buildErrorInfo(HttpStatus code, String message, HttpServletRequest req){
//		ErrorInfo<String> info = new ErrorInfo<>(code.value(),message);
		MyRespBundle<String> myRespBundle = new MyRespBundle<>();
		myRespBundle.setCode(code.value());
		myRespBundle.setMessage(message);
		myRespBundle.setVersion(VersionUtil.getVersion());
		myRespBundle.setTimestamp(Instant.now().toEpochMilli());
//		if(req != null){
//			info.setUrl(req.getRequestURL().toString());
//		}
		return myRespBundle;
	}
	

}
