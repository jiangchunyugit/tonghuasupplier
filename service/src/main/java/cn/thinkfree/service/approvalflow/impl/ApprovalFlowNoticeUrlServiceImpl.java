package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowNoticeUrlMapper;
import cn.thinkfree.database.model.ApprovalFlowNoticeUrl;
import cn.thinkfree.database.model.ApprovalFlowNoticeUrlExample;
import cn.thinkfree.service.approvalflow.ApprovalFlowNoticeUrlService;
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

    @Override
    public void create(String nodeNum, List<ApprovalFlowNoticeUrl> noticeUrls) {
        if (null != noticeUrls){
            for (ApprovalFlowNoticeUrl noticeUrl : noticeUrls){
                noticeUrl.setId(null);
                noticeUrl.setNodeNum(nodeNum);
                noticeUrlMapper.insertSelective(noticeUrl);
            }
        }
    }

    @Override
    public void deleteByNodeNums(List<String> nodeNums) {
        if (nodeNums != null && nodeNums.size() > 0 ){
            ApprovalFlowNoticeUrlExample example = new ApprovalFlowNoticeUrlExample();
            example.createCriteria().andNodeNumIn(nodeNums);
            noticeUrlMapper.deleteByExample(example);
        }
    }
}
