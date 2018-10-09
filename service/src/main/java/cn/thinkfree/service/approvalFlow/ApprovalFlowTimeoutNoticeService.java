package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.model.ApprovalFlowTimeoutNotice;

import java.util.List;

public interface ApprovalFlowTimeoutNoticeService {

    void create(String nodeNum, List<ApprovalFlowTimeoutNotice> timeoutNotices);
}
