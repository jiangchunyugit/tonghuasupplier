package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowConfigLogMapper;
import cn.thinkfree.database.mapper.ApprovalFlowOptionsMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ApprovalFlowDetailVo;
import cn.thinkfree.database.vo.ApprovalFlowNodeVo;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigLogService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowNodeService;
import cn.thinkfree.service.approvalFlow.UserRoleSetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApprovalFlowConfigLogServiceImpl implements ApprovalFlowConfigLogService {

    @Resource
    private ApprovalFlowConfigLogMapper configLogMapper;
    @Resource
    private ApprovalFlowConfigService configService;
    @Resource
    private ApprovalFlowNodeService nodeService;
    @Resource
    private UserRoleSetService userRoleSetService;


    @Override
    public ApprovalFlowDetailVo detail(String approvalFlowNum) {
        ApprovalFlow approvalFlow = configService.findByNum(approvalFlowNum);
        List<ApprovalFlowConfigLog> configLogs = findByApprovalFlowNumOrderVersionAsc(approvalFlowNum);
        ApprovalFlowConfigLog configLog = configLogs.get(configLogs.size() - 1);
        List<ApprovalFlowNodeVo> nodeVos = nodeService.findByExternalUniqueCode(configLog.getRecordUniqueCode());
        List<UserRoleSet> userRoleSets = userRoleSetService.findAll();

        ApprovalFlowDetailVo detailVo = new ApprovalFlowDetailVo();
        detailVo.setApprovalFlow(approvalFlow);
        detailVo.setConfigLogs(configLogs);
        detailVo.setNodes(nodeVos);
        detailVo.setUserRoleSets(userRoleSets);
        return detailVo;
    }

    private List<ApprovalFlowConfigLog> findByApprovalFlowNumOrderVersionAsc(String approvalFlowNum){
        ApprovalFlowConfigLogExample configLogExample = new ApprovalFlowConfigLogExample();
        configLogExample.createCriteria().andApprovalFlowNumEqualTo(approvalFlowNum);
        configLogExample.setOrderByClause("version asc");
        return configLogMapper.selectByExample(configLogExample);
    }
}
