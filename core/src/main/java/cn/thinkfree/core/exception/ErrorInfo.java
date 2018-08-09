package cn.thinkfree.core.exception;

/**
 * 消息载体
 * @param <T>
 */
public class ErrorInfo<T> {
	
	public static final Integer OK = 0;
    public static final Integer ERROR = 100;
    
    public ErrorInfo() {
	}
    
    public ErrorInfo(Integer code, String message) {
    	this.code = code;
    	this.message = message;
   	}
    
    private Integer code;
    private String message;
    private String url;
    private T data;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

}
