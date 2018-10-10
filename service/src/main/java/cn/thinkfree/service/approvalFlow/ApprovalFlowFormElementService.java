package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.model.ApprovalFlowFormElement;

import java.util.List;

public interface ApprovalFlowFormElementService {

    void create(String nodeNum, List<ApprovalFlowFormElement> formElements);
}
