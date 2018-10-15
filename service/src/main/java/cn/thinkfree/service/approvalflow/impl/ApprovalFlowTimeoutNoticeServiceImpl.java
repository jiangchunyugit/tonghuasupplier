package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowTimeoutNoticeMapper;
import cn.thinkfree.database.model.ApprovalFlowTimeoutNotice;
import cn.thinkfree.database.model.ApprovalFlowTimeoutNoticeExample;
import cn.thinkfree.service.approvalflow.ApprovalFlowTimeoutNoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点超时提醒服务层
 */
@Service
@Transactional
public class ApprovalFlowTimeoutNoticeServiceImpl implements ApprovalFlowTimeoutNoticeService {

    @Resource
    private ApprovalFlowTimeoutNoticeMapper timeoutNoticeMapper;

    /**
     * 创建审批流节点超时提醒
     * @param nodeNum 审批流节点编号
     * @param timeoutNotices 审批流节点超时提醒
     */
    @Override
    public void create(String nodeNum, List<ApprovalFlowTimeoutNotice> timeoutNotices) {
        if (timeoutNotices != null) {
            for (ApprovalFlowTimeoutNotice timeoutNotice : timeoutNotices){
                timeoutNotice.setId(null);
                timeoutNotice.setNodeNum(nodeNum);
                timeoutNoticeMapper.insertSelective(timeoutNotice);
            }
        }
    }

    @Override
    public void deleteByNodeNums(List<String> nodeNums) {
        if (nodeNums != null && nodeNums.size() > 0 ) {
            ApprovalFlowTimeoutNoticeExample example = new ApprovalFlowTimeoutNoticeExample();
            example.createCriteria().andNodeNumIn(nodeNums);
            timeoutNoticeMapper.deleteByExample(example);
        }
    }
}
