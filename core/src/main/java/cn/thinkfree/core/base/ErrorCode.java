package cn.thinkfree.core.base;

/**
 * 通用错误码
 *
 * @author win10
 */
public enum ErrorCode {

    OK(1000, ""),
    FAIL(1001, "操作失败"),
    RPC_ERROR(1002, "远程调度失败"),
    USER_NOT_FOUND(1003, "用户不存在"),
    USER_PASSWORD_ERROR(1004, "密码错误"),
    GET_TOKEN_FAIL(1005, "获取token失败"),
    TOKEN_IS_NOT_MATCH_USER(1006, "请使用自己的token进行接口请求"),
    TOKEN_IS_NOT_LOSE_EFFICACY(1500, "请使用自己的token进行接口请求"),
    PARAMETER_ERROR(8888, "参数错误，请检查接口参数"),
    UNKNOWN_EXCEPTION(9999, "未知异常，请检查接口参数"),
    ;
    private int code;
    private String msg;


    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ErrorCode codeOf(int code) {
        for (ErrorCode state : values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        return null;
    }
}
