package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowMessageLogMapper;
import cn.thinkfree.database.model.ApprovalFlowMessageLog;
import cn.thinkfree.service.approvalflow.ApprovalFlowMessageLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 审批流消息记录服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/22 17:50
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ApprovalFlowMessageLogServiceImpl implements ApprovalFlowMessageLogService {

    @Resource
    private ApprovalFlowMessageLogMapper messageLogMapper;

    @Override
    public void create(ApprovalFlowMessageLog messageLog) {
        messageLog.setCreateTime(new Date());
        messageLog.setReadState(0);

        messageLogMapper.insertSelective(messageLog);
    }
}
