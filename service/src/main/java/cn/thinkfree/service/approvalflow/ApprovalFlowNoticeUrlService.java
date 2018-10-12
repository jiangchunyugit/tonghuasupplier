package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.ApprovalFlowNoticeUrl;

import java.util.List;

public interface ApprovalFlowNoticeUrlService {

    void create(String nodeNum, List<ApprovalFlowNoticeUrl> noticeUrls);
}
