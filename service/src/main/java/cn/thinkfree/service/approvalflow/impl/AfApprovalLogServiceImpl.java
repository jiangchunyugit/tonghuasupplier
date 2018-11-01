package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfApprovalLogMapper;
import cn.thinkfree.database.model.AfApprovalLog;
import cn.thinkfree.database.model.AfApprovalLogExample;
import cn.thinkfree.service.approvalflow.AfApprovalLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 10:40
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfApprovalLogServiceImpl implements AfApprovalLogService {

    @Resource
    private AfApprovalLogMapper approvalLogMapper;


    @Override
    public AfApprovalLog findByNo(String approvalNo) {
        AfApprovalLogExample example = new AfApprovalLogExample();
        example.createCriteria().andApprovalNoEqualTo(approvalNo);
        List<AfApprovalLog> approvalLogs = approvalLogMapper.selectByExample(example);
        return approvalLogs != null && approvalLogs.size() > 0 ? approvalLogs.get(0) : null;
    }

    @Override
    public List<AfApprovalLog> findByInstanceNo(String instanceNo) {
        AfApprovalLogExample example = new AfApprovalLogExample();
        example.createCriteria().andInstanceNoEqualTo(instanceNo);
        example.setOrderByClause("sort asc");
        return approvalLogMapper.selectByExample(example);
    }

    @Override
    public AfApprovalLog findByInstanceNoAndSort(String instanceNo, Integer sort) {
        AfApprovalLogExample example = new AfApprovalLogExample();
        example.createCriteria().andInstanceNoEqualTo(instanceNo).andSortEqualTo(sort);
        List<AfApprovalLog> approvalLogs = approvalLogMapper.selectByExample(example);
        return approvalLogs != null && approvalLogs.size() > 0 ? approvalLogs.get(0) : null;
    }

    @Override
    public void updateByPrimaryKey(AfApprovalLog approvalLog) {
        approvalLogMapper.updateByPrimaryKey(approvalLog);
    }

    @Override
    public List<AfApprovalLog> findByUserId(String userId) {
        AfApprovalLogExample example = new AfApprovalLogExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return approvalLogMapper.selectByExample(example);
    }

    @Override
    public void create(List<AfApprovalLog> approvalLogs) {
        for (AfApprovalLog approvalLog : approvalLogs) {
            insert(approvalLog);
        }
    }

    private void insert(AfApprovalLog approvalLog) {
        approvalLogMapper.insertSelective(approvalLog);
    }
}
