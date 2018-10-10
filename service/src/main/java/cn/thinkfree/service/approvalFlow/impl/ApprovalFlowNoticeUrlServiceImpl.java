package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowNoticeUrlMapper;
import cn.thinkfree.database.model.ApprovalFlowNoticeUrl;
import cn.thinkfree.service.approvalFlow.ApprovalFlowNoticeUrlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点发送消息地址服务层
 */
@Service
@Transactional
public class ApprovalFlowNoticeUrlServiceImpl implements ApprovalFlowNoticeUrlService {

    @Resource
    private ApprovalFlowNoticeUrlMapper noticeUrlMapper;

    /**
     * 创建审批流节点发送消息地址
     * @param nodeNum
     * @param noticeUrls
     */
    @Override
    public void create(String nodeNum, List<ApprovalFlowNoticeUrl> noticeUrls) {
        if (null != noticeUrls){
            for (ApprovalFlowNoticeUrl noticeUrl : noticeUrls){
                noticeUrl.setId(0);
                noticeUrl.setNodeNum(nodeNum);
                noticeUrlMapper.insert(noticeUrl);
            }
        }
    }
}
