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
}
