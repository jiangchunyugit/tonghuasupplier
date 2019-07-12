package cn.tonghua.core.exception;

public class MyException extends RuntimeException {

    private String desc;

    private static final long serialVersionUID = 6525461640799286507L;

    public MyException(String message) {
        super(message);
        this.desc= message;
    }

    public MyException(String message, Throwable e) {
        super(message, e);
    }

    public String getDesc(){
        return this.desc;
    }
}
