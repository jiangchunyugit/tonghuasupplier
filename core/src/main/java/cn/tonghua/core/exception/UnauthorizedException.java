package cn.tonghua.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 403
 * 表示用户得到授权（与401错误相对），但是访问是被禁止的。
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "权限不足")
public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 6525461640799286507L;

	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(String message, Throwable e) {
		super(message, e);
	}
}
