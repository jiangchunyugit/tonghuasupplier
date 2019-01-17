package cn.thinkfree.database.constants;

public enum  RoleScope {

    COMMON(7,"通用"),
    ROOT(4,"总部"),
    PROVINCE(2,"省级"),
    CITY(1,"市级");

    public Integer code;
    public String desc;
    RoleScope(Integer code,String mes) {
        this.code = code;
        this.desc = mes;
    }
}
