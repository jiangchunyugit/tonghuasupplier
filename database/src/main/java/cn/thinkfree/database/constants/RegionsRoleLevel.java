package cn.thinkfree.database.constants;

/**
 * 该处与 RoleScope 形成一致 但不复用是因为有歧义
 * @see RoleScope
 */
public enum  RegionsRoleLevel {

    MAX(7,"总部及全部"),
    Branch(4,"分公司级别"),
    City(2,"城市站级别"),
    Store(1,"门店级别");

    public String mes;
    public Integer code;
    RegionsRoleLevel(Integer code , String desc) {
        this.code = code;
        this.mes = desc;
    }
}
