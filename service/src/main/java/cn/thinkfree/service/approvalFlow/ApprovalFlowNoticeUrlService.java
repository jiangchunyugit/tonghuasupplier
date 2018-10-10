package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.model.ApprovalFlowNoticeUrl;

import java.util.List;

public interface ApprovalFlowNoticeUrlService {

    void create(String nodeNum, List<ApprovalFlowNoticeUrl> noticeUrls);
}
