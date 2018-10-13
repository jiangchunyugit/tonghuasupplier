package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowOption;

import java.util.List;

public interface ApprovalFlowOptionService {

    void create(String nodeNum, List<ApprovalFlowOption> options);
}
