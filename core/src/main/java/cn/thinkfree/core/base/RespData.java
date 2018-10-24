package cn.thinkfree.core.base;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.exception.CommonException;

/**
 * 公共返回结果类
 *
 * @author
 */
public class RespData {

    private RespData() {
    }

    /**
     * 成功
     *
     * @return
     */
    public static MyRespBundle<String> success() {
        return success("success", ErrorCode.OK);
    }


    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> MyRespBundle<T> success(T data) {
        return success(data, ErrorCode.OK);
    }

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> MyRespBundle<T> success(T data, ErrorCode code) {
        MyRespBundle<T> myRespBundle = new MyRespBundle<>();
        myRespBundle.setCode(code.getCode());
        myRespBundle.setData(data);
        myRespBundle.setMessage(code.getMsg());
        return myRespBundle;
    }

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> MyRespBundle<T> success(T data, ErrorCode code, String msg) {
        MyRespBundle<T> myRespBundle = new MyRespBundle<>();
        myRespBundle.setCode(code.getCode());
        myRespBundle.setData(data);
        myRespBundle.setMessage(msg);
        return myRespBundle;
    }

    /**
     * 失败
     *
     * @param code
     * @param <T>
     * @return
     */
    public static <T> MyRespBundle<T> error(ErrorCode code) {
        MyRespBundle<T> myRespBundle = new MyRespBundle<>();
        myRespBundle.setCode(code.getCode());
        myRespBundle.setData(null);
        myRespBundle.setMessage(code.getMsg());
        return myRespBundle;
    }

    /**
     * 失败
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> MyRespBundle<T> error(String msg) {
        MyRespBundle<T> myRespBundle = new MyRespBundle<>();
        myRespBundle.setData(null);
        myRespBundle.setMessage(msg);
        return myRespBundle;
    }

    /**
     * 失败
     *
     * @param msg
     * @param code
     * @param <T>
     * @return
     */
    public static <T> MyRespBundle<T> error(int code, String msg) {
        MyRespBundle<T> myRespBundle = new MyRespBundle<>();
        myRespBundle.setCode(code);
        myRespBundle.setData(null);
        myRespBundle.setMessage(msg);
        return myRespBundle;
    }

    /**
     * 失败
     *
     * @param e
     * @param <T>
     * @return
     */
    public static <T> MyRespBundle<T> error(Exception e) {
        ErrorCode code = ErrorCode.UNKNOWN_EXCEPTION;
        if (e instanceof CommonException) {
            MyRespBundle<T> myRespBundle = new MyRespBundle<>();
            myRespBundle.setCode(((CommonException) e).getCode());
            myRespBundle.setData(null);
            myRespBundle.setMessage(((CommonException) e).getMsg());
            return myRespBundle;
        }
        MyRespBundle<T> myRespBundle = new MyRespBundle<>();
        myRespBundle.setCode(code.getCode());
        myRespBundle.setData(null);
        myRespBundle.setMessage(code.getMsg());
        return myRespBundle;
    }
}
