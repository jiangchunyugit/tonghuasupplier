package cn.tonghua.database.utils;

public enum FileResourceEnabled {

    ONE_true(1,"是"),
    ZEROR_false(0,"否"),
    Disabled(2,"禁用");

    public final Integer code;
    public final String desc;
    FileResourceEnabled(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Short shortVal(){
        return code.shortValue();
    }
}
