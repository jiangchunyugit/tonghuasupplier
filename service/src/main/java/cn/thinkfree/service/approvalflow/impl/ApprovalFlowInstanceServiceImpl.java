package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.service.approvalflow.ApprovalFlowInstanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName ApprovalFlowInstanceServiceImpl
 * @Description TODO
 * @Author song
 * @Data 2018/10/12 17:35
 * @Version 1.0
 */
@Service
@Transactional(rollbackFor = {RuntimeException.class})
public class ApprovalFlowInstanceServiceImpl implements ApprovalFlowInstanceService {

}
