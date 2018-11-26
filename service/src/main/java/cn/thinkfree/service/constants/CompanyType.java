package cn.thinkfree.service.constants;

/**
 * @author ying007
 * 公司入驻审批状态 0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金6已交保证金 7入驻成功
 */
public enum CompanyType {
 
    SJ(0,"设计公司"),
   
    BD(1,"装修公司");
    
    private final Integer code;
    private final String mes;

    CompanyType(Integer code ,String mes){
        this.code = code;
        this.mes = mes;
    }
    public String stringVal(){
        return getCode().toString();
    }
	public Integer getCode() {
		return code;
	}
	public String getMes() {
		return mes;
	}

    public static String getNm(String value) {
        CompanyType[] businessModeEnums = values();
        for (CompanyType businessModeEnum : businessModeEnums) {
            if ((businessModeEnum.code+"").equals(value)) {
                return businessModeEnum.name();
            }
        }
        return null;
    }
}
