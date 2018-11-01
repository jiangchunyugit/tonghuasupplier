package cn.thinkfree.database.constants;

/**
 * 请务必按照序号排放
 * 因为其他地方有依赖序号及排放顺序的翻转
 */
public enum  UserRegisterType {
    /**
     * 管理员
     */
    Admin(0,"管理员"),
    /**
     * 个人用户
     */
    Personal(1,"个人"),
    /**
     * 企业用户
     */
    Enterprise(2,"企业"),
    /**
     * 业主
     */
    Customer(3,"业主"),
    /**
     * 企业员工
     */
    Staff(4,"企业员工"),
    /**
     * 平台运营人员
     */
    Platform(5,"平台运营");

    public final Integer code;
    public final String mes;
    UserRegisterType(Integer code, String mes) {
        this.code = code;
        this.mes = mes;
    }



    public Short shortVal(){
        return this.code.shortValue();
    }
}
