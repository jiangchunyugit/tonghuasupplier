package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowMessageLog;

/**
 * 审批流消息记录服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/22 17:49
 */
public interface ApprovalFlowMessageLogService {

    /**
     * 创建审批消息
     * @param messageLog 审批消息
     */
    void create(ApprovalFlowMessageLog messageLog);
}
