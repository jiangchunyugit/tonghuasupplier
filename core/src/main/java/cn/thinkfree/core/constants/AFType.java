package cn.thinkfree.core.constants;


/**
 * 审批流类型
 */
public enum  AFType {

    DAILY_REPORT("DAILY_REPORT", "日常汇报"),
    START_APPLICATION("START_APPLICATION", "开工申请"),
    START_REPORT("START_REPORT", "开工报告"),
    CHECK_APPLICATION("CHECK_APPLICATION", "验收申请"),
    CHECK_REPORT("CHECK_REPORT", "验收报告"),
    RECTIFICATION_NOTIFICATION("RECTIFICATION_NOTIFICATION", "整改通知"),
    CHANGE_ORDER("CHANGE_ORDER", "变更单"),
    POSTPONE_ORDER("POSTPONE_ORDER", "延期单"),
    RECTIFICATION_SUCCESS("RECTIFICATION_SUCCESS", "整改完成");


    public final String num;
    public final String name;

    AFType(String num, String name){
        this.num = num;
        this.name = name;
    }
}
