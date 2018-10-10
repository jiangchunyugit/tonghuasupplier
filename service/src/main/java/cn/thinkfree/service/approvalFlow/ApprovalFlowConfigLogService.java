package cn.thinkfree.service.approvalFlow;

import cn.thinkfree.database.model.ApprovalFlow;
import cn.thinkfree.database.vo.ApprovalFlowDetailVo;
import cn.thinkfree.database.vo.ApprovalFlowNodeVo;

import java.util.List;


public interface ApprovalFlowConfigLogService {

    ApprovalFlowDetailVo detail(String approvalFlowNum);

    void create(ApprovalFlow approvalFlow, List<ApprovalFlowNodeVo> approvalFlowNodes);

    void deleteByApprovalFlowNum(String approvalFlowNum);
}
