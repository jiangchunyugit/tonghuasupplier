package cn.thinkfree.service.approvalFlow;

import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.model.ApprovalFlowConfigLog;
import cn.thinkfree.database.vo.ApprovalFlowDetailVo;
import cn.thinkfree.database.vo.ApprovalFlowNodeVo;

import java.util.List;


public interface ApprovalFlowConfigLogService {

    ApprovalFlowDetailVo detail(String approvalFlowNum);

    void create(ApprovalFlowConfig config, List<ApprovalFlowNodeVo> nodeVos);

    void deleteByApprovalFlowNum(String approvalFlowNum);

    ApprovalFlowConfigLog findLastVersionByApprovalFlowNum(String approvalFlowNum);
}
