package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowApprovalLog;

import java.util.List;

/**
 * 审批流审批日志
 *
 * @author song
 * @version 1.0
 * @date 2018/10/17 17:00
 */
public interface ApprovalFlowApprovalLogService {

    /**
     * 根据审批流实例编号查询有效审批流日志
     * @param instanceNum 审批流实例编号
     * @return 审批流日志
     */
    List<ApprovalFlowApprovalLog> findByInstanceNum(String instanceNum);
}
