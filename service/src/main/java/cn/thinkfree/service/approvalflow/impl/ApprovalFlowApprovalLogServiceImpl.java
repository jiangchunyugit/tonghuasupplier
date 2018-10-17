package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowApprovalLogMapper;
import cn.thinkfree.database.model.ApprovalFlowApprovalLog;
import cn.thinkfree.database.model.ApprovalFlowApprovalLogExample;
import cn.thinkfree.service.approvalflow.ApprovalFlowApprovalLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流审批日志
 *
 * @author song
 * @version 1.0
 * @date 2018/10/17 17:02
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ApprovalFlowApprovalLogServiceImpl implements ApprovalFlowApprovalLogService {

    @Resource
    private ApprovalFlowApprovalLogMapper approvalLogMapper;

    @Override
    public List<ApprovalFlowApprovalLog> findByInstanceNum(String instanceNum) {
        ApprovalFlowApprovalLogExample example = new ApprovalFlowApprovalLogExample();
        example.createCriteria().andInstanceNumEqualTo(instanceNum).andIsInvalidEqualTo(1);
        return approvalLogMapper.selectByExample(example);
    }
}
