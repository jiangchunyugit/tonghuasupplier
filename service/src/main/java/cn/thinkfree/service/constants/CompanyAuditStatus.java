package cn.thinkfree.service.constants;

import java.util.Map;

/**
 * @author ying007
 * 公司入驻审批状态 0待激活1已激活2财务审核中3财务审核成功4财务审核失败5待交保证金6已交保证金 7入驻成功
 */
public enum CompanyAuditStatus {
    /**
     * 待激活
     */
    NOTACTIVATION(0,"待激活"),
    /**
     * 已激活
     */
    ACTIVATION(1,"已激活"),
    /**
     * 财务审核中
     */
    CHECKING(2,"财务审核中"),
    /**
     * 财务审核成功
     */
    SUCCESSCHECK(3,"财务审核成功"),
    /**
     * 财务审核失败
     */
    FAILCHECK(4,"财务审核失败"),
    /**
     * 待交保证金
     */
    NOTPAYBAIL(5,"待交保证金"),
    /**
     * 已交保证金
     */
    PAYBAIL(6,"已交保证金"),
    /**
     * 入驻成功
     */
    SUCCESSJOIN(7,"入驻成功"),
	
	 /**
	  * 8 910';
     * 资质待审核中
     */
	APTITUDEING(8,"资质待审核中"),
    
    
    /**
     * 资质审核通过
     */
	APTITUDETG(9,"资质审核通过 "),
    
    
    /**
     * 资质审核不通过
     */
    SUCCESSJOSB(10,"资质审核不通过");

    public final Integer code;
    public final String mes;

    CompanyAuditStatus(Integer code ,String mes){
        this.code = code;
        this.mes = mes;
    }
    public String stringVal(){
        return code.toString();
    }
    
    public static String getDesc(Integer value) {  
    	CompanyAuditStatus[] businessModeEnums = values();  
        for (CompanyAuditStatus businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.code == value) {  
                return businessModeEnum.mes;  
            }  
        }  
        return null;  
    }  

}
