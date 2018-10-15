package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowOption;

import java.util.List;

public interface ApprovalFlowOptionService {

    /**
     * 创建审批流节点操作项
     * @param nodeNum 审批流节点编号
     * @param options 审批流节点操作项
     */
    void create(String nodeNum, List<ApprovalFlowOption> options);

    void deleteByNodeNums(List<String> nodeNums);
}
