package cn.thinkfree.service.constants;

/**
 * 结算比例/结算规则审核状态
 */
public enum SettlementStatus {

//状态   1 待审核 2审核通过 3审核不通过 4作废


    /**
     * 待审核
     */
    AuditWait(1,"待审核"),
    /**
     * 审批通过
     */
    AuditPass(2,"审批通过"),
    /**
     * 审批拒绝
     */
    AuditDecline(3,"审批拒绝"),

    /**
     * 作废
     */
    AuditCAN(4,"作废");


    public final Integer code;
    public final String mes;

    SettlementStatus(Integer code , String mes){
        this.code = code;
        this.mes = mes;
    }
    public String getCode(){
         return code.toString();
    }

    /**
     * 字典翻译
     * @param value
     * @return
     */
    public static String getDesc(String value) {  
    	SettlementStatus[] businessModeEnums = values();  
        for (SettlementStatus businessModeEnum : businessModeEnums) {  
            if ((businessModeEnum.code+"").equals(value)) {  
                return businessModeEnum.mes;  
            }  
        }  
        return null;  
    }  
}
