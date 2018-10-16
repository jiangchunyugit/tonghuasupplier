package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.vo.ApprovalFlowNodeVO;
import cn.thinkfree.database.vo.NodeRoleSequenceVo;

import java.util.List;

public interface ApprovalFlowNodeService {

    List<ApprovalFlowNodeVO> findVoByConfigLogNum(String configLogNum);

    List<ApprovalFlowNode> findByConfigLogNum(String configLogNum);

    void create(String configLogNum, List<ApprovalFlowNodeVO> approvalFlowNodeVOs);

    void deleteByConfigLogNums(List<String> configLogNums);

}
