package cn.thinkfree.database.constants;

public enum UserEnabled {

    Enabled_true(1,"启用"),
    Enabled_false(0,"不启用");

    public Integer code;
    public String desc;
    UserEnabled(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Short shortVal(){
        return code.shortValue();
    }
}
