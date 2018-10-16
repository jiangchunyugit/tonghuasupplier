package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowTimeoutNotice;

import java.util.List;

public interface ApprovalFlowTimeoutNoticeService {

    void create(String nodeNum, List<ApprovalFlowTimeoutNotice> timeoutNotices);

    void deleteByNodeNums(List<String> nodeNums);

}
