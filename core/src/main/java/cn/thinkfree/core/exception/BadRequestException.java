package cn.thinkfree.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 400
 * [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "操作错误")
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 6525461640799286507L;

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, Throwable e) {
		super(message, e);
	}
}
