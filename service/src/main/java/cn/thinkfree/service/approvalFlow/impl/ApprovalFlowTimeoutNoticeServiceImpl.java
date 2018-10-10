package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowTimeoutNoticeMapper;
import cn.thinkfree.database.model.ApprovalFlowTimeoutNotice;
import cn.thinkfree.service.approvalFlow.ApprovalFlowTimeoutNoticeService;
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
                timeoutNotice.setId(0);
                timeoutNotice.setExternalUniqueCode(nodeNum);
                timeoutNoticeMapper.insert(timeoutNotice);
            }
        }
    }
}
