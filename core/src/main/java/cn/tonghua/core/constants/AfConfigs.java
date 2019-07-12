package cn.tonghua.core.constants;

/**
 * 审批流类型
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 16:14
 */
public enum AfConfigs {

    START_APPLICATION("START_APPLICATION", "开工申请"),
    START_REPORT("START_REPORT", "开工报告"),
    COMPLETE_APPLICATION("COMPLETE_APPLICATION", "完工申请"),
    CHECK_APPLICATION("CHECK_APPLICATION", "验收申请"),
    CHECK_REPORT("CHECK_REPORT", "验收报告"),
    PROBLEM_RECTIFICATION("PROBLEM_RECTIFICATION", "问题整改"),
    RECTIFICATION_COMPLETE("NOTIFICATION_COMPLETE", "整改完成审批"),
    CHANGE_ORDER("CHANGE_ORDER", "变更单"),
    CHANGE_COMPLETE("CHANGE_COMPLETE", "变更完成审批"),
    DELAY_ORDER("DELAY_ORDER", "延期单");

    public final String configNo;
    public final String name;

    AfConfigs(String configNo, String name){
        this.configNo = configNo;
        this.name = name;
    }
}
