package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.model.ApprovalFlowOption;

import java.util.List;

public interface ApprovalFlowOptionService {

    void create(String nodeNum, List<ApprovalFlowOption> options);
}
