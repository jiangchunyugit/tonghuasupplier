package cn.thinkfree.core.constants;

/**
 * 审批流相关常量
 *
 * @author song
 * @version 1.0
 * @date 2018/10/29 15:31
 */
public class AfConstants {

    /**
     * 审批流审批状态：正在进行中
     */
    public static final int APPROVAL_STATUS_START = 1;
    /**
     * 审批流审批状态：同意
     */
    public static final int APPROVAL_STATUS_SUCCESS = 2;
    /**
     * 审批流审批状态：拒绝
     */
    public static final int APPROVAL_STATUS_FAIL = 3;
    /**
     * 审批类型：进度验收
     */
    public static final String APPROVAL_TYPE_SCHEDULE_APPROVAL = "APPROVAL_TYPE_SCHEDULE_APPROVAL";
    /**
     * 审批类型：问题整改
     */
    public static final String APPROVAL_TYPE_PROBLEM_RECTIFICATION = "APPROVAL_TYPE_PROBLEM_RECTIFICATION";
    /**
     * 审批类型：施工变更
     */
    public static final String APPROVAL_TYPE_CONSTRUCTION_CHANGE = "APPROVAL_TYPE_CONSTRUCTION_CHANGE";
    /**
     * 审批类型：延期确认
     */
    public static final String APPROVAL_TYPE_DELAY_VERIFY = "APPROVAL_TYPE_DELAY_VERIFY";
    /**
     * 审批状态：发起
     */
    public static final int APPROVAL_OPTION_START = 1;
    /**
     * 审批状态：同意
     */
    public static final int APPROVAL_OPTION_AGREE = 2;
    /**
     * 审批状态：拒绝
     */
    public static final int APPROVAL_OPTION_REFUSAL = 3;
    /**
     * 审批状态：未审批
     */
    public static final int APPROVAL_OPTION_UNAPPROVAL = 4;
}
