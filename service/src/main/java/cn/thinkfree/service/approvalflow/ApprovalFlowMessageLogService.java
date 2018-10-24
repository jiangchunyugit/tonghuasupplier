package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowMessageLog;
import cn.thinkfree.database.vo.ApprovalFlowMessageLogVO;

import java.util.List;

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

    /**
     * 根据用户id查询用户消息
     * @param userId 用户Id
     * @return 用户消息
     */
    List<ApprovalFlowMessageLogVO> findByUserId(String userId);

    /**
     * 未读消息总数
     * @param userId 用户Id
     * @return 未读消息总数
     */
    Long countOfUnread(String userId);
}
