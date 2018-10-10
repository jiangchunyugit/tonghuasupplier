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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 审批流配置记录服务层
 */
@Service
@Transactional
public class ApprovalFlowConfigLogServiceImpl implements ApprovalFlowConfigLogService {

    @Resource
    private ApprovalFlowConfigLogMapper configLogMapper;
    @Resource
    private ApprovalFlowConfigService configService;
    @Resource
    private ApprovalFlowNodeService nodeService;
    @Resource
    private RoleService roleService;

    /**
     * 根据审批流编号查询当前版本的审批流信息
     * @param approvalFlowNum 审批流编号
     * @return 当前版本的审批流信息
     */
    @Override
    public ApprovalFlowDetailVo detail(String approvalFlowNum) {
        ApprovalFlowConfig config = configService.findByNum(approvalFlowNum);
        List<ApprovalFlowConfigLog> configLogs = findByApprovalFlowNumOrderByVersionAsc(approvalFlowNum);
        ApprovalFlowConfigLog configLog = configLogs.get(configLogs.size() - 1);
        List<ApprovalFlowNodeVo> nodeVos = nodeService.findByConfigLogNum(configLog.getNum());
        List<UserRoleSet> userRoleSets = roleService.findAll();

        ApprovalFlowDetailVo detailVo = new ApprovalFlowDetailVo();
        detailVo.setConfig(config);
        detailVo.setConfigLogs(configLogs);
        detailVo.setNodeVos(nodeVos);
        detailVo.setUserRoleSets(userRoleSets);
        return detailVo;
    }

    /**
     * 根据审批流编号查询所有版本的审批流信息，并以版本号正序排序
     * @param approvalFlowNum 审批流编号
     * @return 审批流信息
     */
    private List<ApprovalFlowConfigLog> findByApprovalFlowNumOrderByVersionAsc(String approvalFlowNum){
        ApprovalFlowConfigLogExample configLogExample = new ApprovalFlowConfigLogExample();
        configLogExample.createCriteria().andApprovalFlowNumEqualTo(approvalFlowNum);
        configLogExample.setOrderByClause("version asc");
        return configLogMapper.selectByExample(configLogExample);
    }

    /**
     * 创建新的审批流配置记录
     * @param config 审批流配置
     * @param approvalFlowNodeVos 审批流节点信息
     */
    @Override
    public void create(ApprovalFlowConfig config, List<ApprovalFlowNodeVo> approvalFlowNodeVos) {
        ApprovalFlowConfigLog configLog = new ApprovalFlowConfigLog();
        configLog.setApprovalFlowNum(config.getApprovalFlowNum());
        configLog.setCreateTime(new Date());
        configLog.setCreateUserId(config.getCreateUserId());
        configLog.setNum(UniqueCodeGenerator.AF_CONFIG_LOG.getCode());
        configLog.setVersion(config.getVersion());
        configLog.setH5Link(config.getH5Link());
        configLog.setH5Resume(config.getH5Resume());
        configLog.setType(config.getType());
        configLog.setCompanyNum(config.getCompanyNum());
        insert(configLog);
        nodeService.create(configLog.getNum(), approvalFlowNodeVos);
    }

    /**
     * 插入单个配置记录
     * @param configLog 审批配置记录
     */
    public void insert(ApprovalFlowConfigLog configLog){
        configLogMapper.insert(configLog);
    }

    @Override
    public void deleteByApprovalFlowNum(String approvalFlowNum) {
        ApprovalFlowConfigLogExample configLogExample = new ApprovalFlowConfigLogExample();
        configLogExample.createCriteria().andApprovalFlowNumEqualTo(approvalFlowNum);

        List<ApprovalFlowConfigLog> configLogs = configLogMapper.selectByExample(configLogExample);
        if (configLogs != null) {
            for(ApprovalFlowConfigLog configLog : configLogs){
                nodeService.deleteByConfigLogNum(configLog.getNum());
            }
        }

        configLogMapper.deleteByExample(configLogExample);
    }
}
