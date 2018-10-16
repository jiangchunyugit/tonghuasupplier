package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.model.ApprovalFlowConfigLog;
import cn.thinkfree.database.vo.ApprovalFlowNodeVO;

import java.util.List;


public interface ApprovalFlowConfigLogService {

    void create(ApprovalFlowConfig config, List<ApprovalFlowNodeVO> nodeVos);

    void deleteByApprovalFlowNum(String approvalFlowNum);

    ApprovalFlowConfigLog findByConfigNumAndVersion(String configNum, int version);

    ApprovalFlowConfigLog findLastVersionByApprovalFlowNum(String approvalFlowNum);

    List<ApprovalFlowConfigLog> findByApprovalFlowNumOrderByVersionAsc(String approvalFlowNum);
}
