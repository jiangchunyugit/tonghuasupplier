package cn.thinkfree.database.constants;

/**
 * 用户级别描述
 */
public enum  UserLevel {

    Creator(0,"超级用户"),
    Company_Admin(1,"公司根账号"),
    Company_Province(2,"公司省账号"),
    Company_City(3,"公司市账号"),
    Company_City_Master(4,"公司市级兼省级");

    public Integer code;
    public String desc;
    UserLevel(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
