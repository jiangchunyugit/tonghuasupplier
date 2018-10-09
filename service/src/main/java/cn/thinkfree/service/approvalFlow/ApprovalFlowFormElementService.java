package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.model.ApprovalFlowFormElement;
import cn.thinkfree.database.model.ApprovalFlowNodeRole;

import java.util.List;

public interface ApprovalFlowFormElementService {

    void create(String nodeNum, List<ApprovalFlowFormElement> formElements);
}
