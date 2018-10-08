package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowMapper;
import cn.thinkfree.database.model.ApprovalFlow;
import cn.thinkfree.database.model.ApprovalFlowExample;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigLogService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApprovalFlowConfigServiceImpl implements ApprovalFlowConfigService {

    @Resource
    private ApprovalFlowMapper approvalFlowMapper;
    @Resource
    private ApprovalFlowConfigLogService configLogService;

    @Override
    public List<ApprovalFlow> list() {
        ApprovalFlowExample approvalFlowExample = new ApprovalFlowExample();
        approvalFlowExample.setOrderByClause("sort asc");
        return approvalFlowMapper.selectByExample(approvalFlowExample);
    }

    @Override
    public ApprovalFlow findByNum(String approvalFlowNum){
        ApprovalFlowExample approvalFlowExample = new ApprovalFlowExample();
        approvalFlowExample.createCriteria().andApprovalFlowNameEqualTo(approvalFlowNum);
        List<ApprovalFlow> approvalFlows = approvalFlowMapper.selectByExample(approvalFlowExample);
        return approvalFlows != null && approvalFlows.size() > 0 ? approvalFlows.get(0) : null;
    }
}
