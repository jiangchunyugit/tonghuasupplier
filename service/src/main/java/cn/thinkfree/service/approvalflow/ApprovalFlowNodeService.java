package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.vo.ApprovalFlowNodeVo;
import cn.thinkfree.database.vo.NodeRoleSequenceVo;

import java.util.List;

public interface ApprovalFlowNodeService {

    List<ApprovalFlowNodeVo> findVoByConfigLogNum(String configLogNum);

    List<ApprovalFlowNode> findByConfigLogNum(String configLogNum);

    void create(String configLogNum, List<ApprovalFlowNodeVo> approvalFlowNodeVos);

    void deleteByConfigLogNums(List<String> configLogNums);

    List<NodeRoleSequenceVo> findNodeRoleSequence(String approvalFlowNum, String companyId, Long projectBigSchedulingId);
}
