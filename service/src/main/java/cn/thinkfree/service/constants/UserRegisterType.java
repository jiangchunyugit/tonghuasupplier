package cn.thinkfree.service.constants;

public enum  UserRegisterType {

    Personal(1,"个人"),

    Enterprise(2,"企业"),

    Staff(3,"企业员工");

    public Integer code;
    public String mes;
    UserRegisterType(Integer code, String mes) {
        this.code = code;
        this.mes = mes;
    }

    public Short shortVal(){
        return this.code.shortValue();
    }
}
