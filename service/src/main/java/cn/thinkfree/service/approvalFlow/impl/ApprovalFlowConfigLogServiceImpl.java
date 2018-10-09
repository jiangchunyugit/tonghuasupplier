package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowConfigLogMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ApprovalFlowDetailVo;
import cn.thinkfree.database.vo.ApprovalFlowNodeVo;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigLogService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowNodeService;
import cn.thinkfree.service.approvalFlow.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
    private RoleService roleService;


    @Override
    public ApprovalFlowDetailVo detail(String approvalFlowNum) {
        ApprovalFlow approvalFlow = configService.findByNum(approvalFlowNum);
        List<ApprovalFlowConfigLog> configLogs = findByApprovalFlowNumOrderByVersionAsc(approvalFlowNum);
        ApprovalFlowConfigLog configLog = configLogs.get(configLogs.size() - 1);
        List<ApprovalFlowNodeVo> nodeVos = nodeService.findByExternalUniqueCode(configLog.getRecordUniqueCode());
        List<UserRoleSet> userRoleSets = roleService.findAll();

        ApprovalFlowDetailVo detailVo = new ApprovalFlowDetailVo();
        detailVo.setApprovalFlow(approvalFlow);
        detailVo.setConfigLogs(configLogs);
        detailVo.setNodes(nodeVos);
        detailVo.setUserRoleSets(userRoleSets);
        return detailVo;
    }

    private List<ApprovalFlowConfigLog> findByApprovalFlowNumOrderByVersionAsc(String approvalFlowNum){
        ApprovalFlowConfigLogExample configLogExample = new ApprovalFlowConfigLogExample();
        configLogExample.createCriteria().andApprovalFlowNumEqualTo(approvalFlowNum);
        configLogExample.setOrderByClause("version asc");
        return configLogMapper.selectByExample(configLogExample);
    }

    @Override
    public void create(ApprovalFlow approvalFlow, List<ApprovalFlowNodeVo> approvalFlowNodeVos) {
        ApprovalFlowConfigLog configLog = new ApprovalFlowConfigLog();
        configLog.setApprovalFlowNum(approvalFlow.getApprovalFlowNum());
        configLog.setCreateTime(new Date());
        configLog.setCreateUserId(approvalFlow.getCreateUserId());
        configLog.setRecordUniqueCode(UniqueCodeGenerator.AF_LOG.getCode());
        configLog.setVersion(approvalFlow.getVersion());
        configLog.setH5Link(approvalFlow.getH5Link());
        configLog.setH5Resume(approvalFlow.getH5Resume());
        configLog.setType(approvalFlow.getType());
        configLog.setCompanyNum(approvalFlow.getCompanyNum());
        insert(configLog);
        nodeService.create(configLog.getRecordUniqueCode(), approvalFlowNodeVos);
    }

    public void insert(ApprovalFlowConfigLog configLog){
        configLogMapper.insert(configLog);
    }
}
