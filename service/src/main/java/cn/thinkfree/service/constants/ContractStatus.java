package cn.thinkfree.service.constants;

/**
 * 合同状态
 */
public enum ContractStatus {

    /**
     * 0草稿
     */
    DraftStatus(0,"草稿"),
    /**
     * 待审批
     */
    WaitAudit(1,"待审批"),
    /**
     * 审批通过
     */
    AuditPass(2,"审批通过"),
    /**
     * 审批拒绝
     */
    AuditDecline(3,"审批拒绝"),
	
	 /**
     * 审批拒绝
     */
    Waitdeposit(4,"待确认保证金"),
    
    /**
     * 审批拒绝
     */
    suredeposit(5,"已确认保证金");
    


    public final Integer code;
    public final String mes;

    ContractStatus(Integer code , String mes){
        this.code = code;
        this.mes = mes;
    }
    public String shortVal(){
         return code.toString();
    }
    
    /**
     * 字典翻译
     * @param value
     * @return
     */
    public static String getDesc(String value) {  
    	ContractStatus[] businessModeEnums = values();  
        for (ContractStatus businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.code.equals(value)) {  
                return businessModeEnum.mes;  
            }  
        }  
        return null;  
    }  
}
