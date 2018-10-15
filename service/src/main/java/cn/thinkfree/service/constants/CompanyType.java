package cn.thinkfree.service.constants;

/**
 * @author ying007
 * 公司入驻审批状态 0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金6已交保证金 7入驻成功
 */
public enum CompanyType {
    /**
     * 待激活
     */
    SJ(0,"设计公司"),
    /**
     * 已激活
     */
    zj(1,"装修公司");
    

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

}
