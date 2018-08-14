package cn.thinkfree.core.constants;

/**
 * 系统日志模块
 * 配合切面和注解
 * @see cn.thinkfree.core.annotation.MySysLog
 */
public enum  SysLogModule {

    PC_USER(1,"用户模块");

    public String mes;
    public Integer code;

    SysLogModule(Integer code, String mes) {
        this.mes = mes;
        this.code = code;
    }
}
