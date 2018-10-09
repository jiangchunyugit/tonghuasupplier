package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.dto.ApprovalFlowConfigLogDTO;
import cn.thinkfree.database.mapper.ApprovalFlowMapper;
import cn.thinkfree.database.model.ApprovalFlow;
import cn.thinkfree.database.model.ApprovalFlowExample;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigLogService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    @Override
    public void edit(ApprovalFlowConfigLogDTO configLogDTO) {
        if (StringUtils.isEmpty(configLogDTO.getApprovalFlowNum())) {
            throw new RuntimeException("审批流编号不能为空");
        }
        ApprovalFlow approvalFlow = findByNum(configLogDTO.getApprovalFlowNum());
        if (approvalFlow == null) {
            throw new RuntimeException("数据无效");
        }
        approvalFlow.setVersion(approvalFlow.getVersion() + 1);
        approvalFlow.setUpdateUserId(configLogDTO.getCreateUserId());
        approvalFlow.setUpdateTime(new Date());
        approvalFlow.setApprovalFlowName(configLogDTO.getApprovalFlowName());
        approvalFlow.setCompanyNum(configLogDTO.getCompanyNum());
        approvalFlow.setH5Link(configLogDTO.getH5Link());
        approvalFlow.setH5Resume(configLogDTO.getH5Resume());
        approvalFlow.setType(configLogDTO.getType());
        save(approvalFlow);
        configLogService.create(approvalFlow, configLogDTO.getApprovalFlowNodeVos());
    }

    public void save(ApprovalFlow approvalFlow){
        approvalFlowMapper.updateByPrimaryKey(approvalFlow);
    }
}
