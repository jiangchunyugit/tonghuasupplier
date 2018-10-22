package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.vo.ApprovalFlowNodeVO;

import java.util.List;

public interface ApprovalFlowNodeService {

    List<ApprovalFlowNodeVO> findVoByConfigLogNum(String configLogNum);

    ApprovalFlowNode findByNum(String num);

    List<ApprovalFlowNode> findByConfigLogNum(String configLogNum);

    void create(String configLogNum, List<ApprovalFlowNodeVO> approvalFlowNodeVOs);

    void deleteByConfigLogNums(List<String> configLogNums);

}
