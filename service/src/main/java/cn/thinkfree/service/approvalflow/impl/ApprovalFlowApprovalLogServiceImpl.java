package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowApprovalLogMapper;
import cn.thinkfree.database.model.ApprovalFlowApprovalLog;
import cn.thinkfree.database.model.ApprovalFlowApprovalLogExample;
import cn.thinkfree.database.model.ApprovalFlowOption;
import cn.thinkfree.service.approvalflow.ApprovalFlowApprovalLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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

    @Override
    public ApprovalFlowApprovalLog create(String instanceNum, String nodeNum, String userId, String roleId, String optionNum, String remark) {
        ApprovalFlowApprovalLog approvalLog = new ApprovalFlowApprovalLog();
        approvalLog.setCreateTime(new Date());
        approvalLog.setInstanceNum(instanceNum);
        approvalLog.setIsInvalid(1);
        approvalLog.setNodeNum(nodeNum);
        approvalLog.setNum(UniqueCodeGenerator.AF_APPROVAL_LOG.getCode());
        approvalLog.setOptionNum(optionNum);
        approvalLog.setRemark(remark);
        approvalLog.setRoleId(roleId);
        approvalLog.setUserId(userId);

        approvalLogMapper.insertSelective(approvalLog);
        return findByNum(approvalLog.getNum());
    }

    private ApprovalFlowApprovalLog findByNum(String num) {
        ApprovalFlowApprovalLogExample example = new ApprovalFlowApprovalLogExample();
        example.createCriteria().andNumEqualTo(num);
        List<ApprovalFlowApprovalLog> approvalLogs = approvalLogMapper.selectByExample(example);
        return approvalLogs != null && approvalLogs.size() > 0 ? approvalLogs.get(0) : null;
    }

    @Override
    public void updateIsInvalidByInstanceNumAndNodeNums(String instanceNum, List<String> nodeNums) {
        ApprovalFlowApprovalLogExample example = new ApprovalFlowApprovalLogExample();
        example.createCriteria().andInstanceNumEqualTo(instanceNum).andNodeNumIn(nodeNums);

        ApprovalFlowApprovalLog approvalLog = new ApprovalFlowApprovalLog();
        approvalLog.setIsInvalid(0);
        approvalLogMapper.updateByExampleSelective(approvalLog, example);
    }
}
