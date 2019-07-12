package cn.tonghua.core.exception;


public class CommonException extends RuntimeException {

    private ErrorCode errorCode;

    private int code;

    private String msg;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public CommonException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
