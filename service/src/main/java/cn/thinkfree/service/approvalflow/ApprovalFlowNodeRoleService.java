package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowNodeRole;

import java.util.List;

public interface ApprovalFlowNodeRoleService {

    void create(String nodeNum, List<ApprovalFlowNodeRole> nodeRoles);

    List<ApprovalFlowNodeRole> findLastVersionByNodeNumAndProjectBigSchedulingId(String nodeNum, Long projectBigSchedulingId);
}
