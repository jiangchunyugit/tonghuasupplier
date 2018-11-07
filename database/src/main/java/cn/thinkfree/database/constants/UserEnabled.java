package cn.thinkfree.database.constants;

public enum UserEnabled {

    Enabled_true(1,"启用"),
    Enabled_false(0,"不启用"),
    Disable(2,"禁用");

    public final Integer code;
    public final String desc;
    UserEnabled(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Short shortVal(){
        return code.shortValue();
    }
}
