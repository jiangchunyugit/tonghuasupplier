package cn.thinkfree.service.platform.designer;

/**
 * @author xusonghui
 * 申请退款服务
 */
public interface ApplyRefundService {

    /**
     * 申请退款
     *
     * @param orderNo    设计订单编号
     * @param userId     用户Id
     * @param reason     退款原因
     * @param money      退款金额
     * @param title      退款标题
     * @param flowNumber 支付流水号
     */
    void applyRefund(String orderNo, String userId, String reason, String money, String title, String flowNumber);

    /**
     * 设计公司同意退款
     *
     * @param refundNo   退款申请记录编号
     * @param optionId   操作人Id
     * @param optionName 操作人名称
     */
    void companyRefundAgree(String refundNo, String optionId, String optionName);

    /**
     * 设计公司驳回退款申请
     *
     * @param refundNo   退款申请记录编号
     * @param optionId   操作人Id
     * @param reason     驳回原因
     * @param optionName 操作人名称
     */
    void companyRefundReject(String refundNo, String optionId, String reason, String optionName);

    /**
     * 设计公司同意退款
     *
     * @param refundNo   退款申请记录编号
     * @param optionId   操作人Id
     * @param optionName 操作人名称
     */
    void platformRefundAgree(String refundNo, String optionId, String optionName);

    /**
     * 设计公司驳回退款申请
     *
     * @param refundNo   退款申请记录编号
     * @param optionId   操作人Id
     * @param reason     驳回原因
     * @param optionName 操作人名称
     */
    void platformRefundReject(String refundNo, String optionId, String reason, String optionName);

    /**
     * 财务平台统一退款申请
     *
     * @param refundNo   申请退款业务编号
     * @param optionId   操作人ID
     * @param optionName 操作人名称
     */
    void financeRefundAgree(String refundNo, String optionId, String optionName);
}
