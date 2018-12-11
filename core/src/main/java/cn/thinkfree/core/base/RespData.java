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
        return success("操作成功", ErrorCode.OK);
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
        myRespBundle.setMsg(code.getMsg());
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
        myRespBundle.setMsg(msg);
        return myRespBundle;
    }

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> MyRespBundle<T> success(T data, String msg) {
        MyRespBundle<T> myRespBundle = new MyRespBundle<>();
        myRespBundle.setCode(ErrorCode.OK.getCode());
        myRespBundle.setData(data);
        myRespBundle.setMsg(msg);
        return myRespBundle;
    }

    /**
     * 失败
     *
     * @param code
     * @return
     */
    public static MyRespBundle error(ErrorCode code) {
        MyRespBundle myRespBundle = new MyRespBundle();
        myRespBundle.setCode(code.getCode());
        myRespBundle.setData(code.getMsg());
        myRespBundle.setMsg(code.getMsg());
        return myRespBundle;
    }

    /**
     * 失败
     *
     * @param msg
     * @return
     */
    public static MyRespBundle error(String msg) {
        MyRespBundle myRespBundle = new MyRespBundle<>();
        myRespBundle.setCode(ErrorCode.FAIL.getCode());
        myRespBundle.setData(msg);
        myRespBundle.setMsg(msg);
        return myRespBundle;
    }

    /**
     * 失败
     *
     * @param msg
     * @param code
     * @return
     */
    public static MyRespBundle error(int code, String msg) {
        MyRespBundle myRespBundle = new MyRespBundle();
        myRespBundle.setCode(code);
        myRespBundle.setData(msg);
        myRespBundle.setMsg(msg);
        return myRespBundle;
    }

}
