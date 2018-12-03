package cn.thinkfree.service.constants;

import cn.thinkfree.database.constants.CompanyAuditStatus;

/**
 * 审核状态
 */
public enum AuditStatus {


    /**
     * 审批通过
     */
    AuditPass(1,"审批通过"),
    /**
     * 审批拒绝
     */
    AuditDecline(0,"审批拒绝"),

    /**
     * 审批中
     */
    AUDITING(2,"审批中");


    public final Integer code;
    public final String mes;

    AuditStatus(Integer code , String mes){
        this.code = code;
        this.mes = mes;
    }
    public String shortVal(){
         return code.toString();
    }

    public static String getDesc(Integer value) {
        AuditStatus[] auditStatus = values();
        for (AuditStatus auditStatu : auditStatus) {
            if (auditStatu.code == value) {
                return auditStatu.mes;
            }
        }
        return null;
    }
}
