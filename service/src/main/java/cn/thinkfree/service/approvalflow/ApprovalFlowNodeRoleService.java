package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowNodeRole;

import java.util.List;

public interface ApprovalFlowNodeRoleService {

    void create(String nodeNum, List<ApprovalFlowNodeRole> nodeRoles);

    List<ApprovalFlowNodeRole> findSendRoleByNodeNum(String nodeNum);

    void deleteByNodeNums(List<String> nodeNums);

    /**
     * 根据审批流节点编号查询接受消息角色
     * @param nodeNum 审批流节点编号
     * @return 接受消息角色
     */
    List<ApprovalFlowNodeRole> findReceiveRoleByNodeNum(String nodeNum);

}
