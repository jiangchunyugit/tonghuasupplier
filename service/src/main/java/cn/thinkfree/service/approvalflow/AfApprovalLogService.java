package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfApprovalLog;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 10:40
 */
public interface AfApprovalLogService {

    AfApprovalLog findByNo(String approvalLogNo);
}
