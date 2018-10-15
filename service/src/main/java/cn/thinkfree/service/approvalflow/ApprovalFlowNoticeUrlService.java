package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowNoticeUrl;

import java.util.List;


/**
 * 审批流节点发送消息地址服务层
 */
public interface ApprovalFlowNoticeUrlService {

    /**
     * 创建审批流节点发送消息地址
     * @param nodeNum 审批流节点编号
     * @param noticeUrls 发送消息地址
     */
    void create(String nodeNum, List<ApprovalFlowNoticeUrl> noticeUrls);

    /**
     * 根据审批流节点编号删除发送消息地址
     * @param nodeNums
     */
    void deleteByNodeNums(List<String> nodeNums);
}
