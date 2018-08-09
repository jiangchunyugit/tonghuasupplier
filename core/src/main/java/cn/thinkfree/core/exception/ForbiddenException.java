package cn.thinkfree.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 401
 * 表示用户没有权限（令牌、用户名、密码错误）。
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "没有权限")
public class ForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 6525461640799286507L;

	public ForbiddenException(String message) {
		super(message);
	}

	public ForbiddenException(String message, Throwable e) {
		super(message, e);
	}
}
