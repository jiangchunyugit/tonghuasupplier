package cn.thinkfree.service.approvalFlow;

import cn.thinkfree.database.dto.ApprovalFlowConfigLogDTO;
import cn.thinkfree.database.model.ApprovalFlowConfig;

import java.util.List;

public interface ApprovalFlowConfigService {

    ApprovalFlowConfig findByNum(String approvalFlowNum);

    List<ApprovalFlowConfig> list();

    void edit(ApprovalFlowConfigLogDTO configLogDTO);

    void add(ApprovalFlowConfigLogDTO configLogDTO);

    void delete(String approvalFlowNum);

}
