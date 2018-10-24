package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.constants.AFAlias;
import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.database.mapper.ApprovalFlowConfigMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.approvalflow.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 审批流配置服务层
 * @author song
 * @date 2018/10/11 17:44
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = {RuntimeException.class})
public class ApprovalFlowConfigServiceImpl implements ApprovalFlowConfigService {

    private static final MyLogger LOGGER = new MyLogger(ApprovalFlowConfigService.class);

    @Resource
    private ApprovalFlowConfigMapper configMapper;
    @Resource
    private ApprovalFlowConfigLogService configLogService;
    @Resource
    private ApprovalFlowNodeService nodeService;
    @Resource
    private RoleService roleService;
    @Resource
    private ApprovalFlowScheduleNodeRoleService scheduleNodeRoleService;
    @Resource
    private ApprovalFlowNodeRoleService nodeRoleService;
    @Resource
    private ApprovalFlowConfigSuperService configSuperService;

    /**
     * 查询所有审批流，并以sort正序
     * @return 所有审批流
     */
    @Override
    public List<ApprovalFlowConfig> list() {
        ApprovalFlowConfigExample configExample = new ApprovalFlowConfigExample();
        configExample.setOrderByClause("sort asc");
        return configMapper.selectByExample(configExample);
    }


    /**
     * 根据审批流编号查询当前版本的审批流信息
     * @param configNum 审批流编号
     * @return 当前版本的审批流信息
     */
    @Override
    public ApprovalFlowConfigLogVO detail(String configNum) {
        ApprovalFlowConfig config = findByNum(configNum);
        List<ApprovalFlowConfigLog> configLogs = configLogService.findByApprovalFlowNumOrderByVersionAsc(configNum);
        ApprovalFlowConfigLog configLog = configLogs.get(configLogs.size() - 1);
        List<ApprovalFlowNodeVO> nodeVos = nodeService.findVoByConfigLogNum(configLog.getNum());
        List<ApprovalFlowConfigSuper> configSupers = configSuperService.findByConfigAlias(config.getAlias());
        List<UserRoleSet> roles = roleService.findAll();

        ApprovalFlowConfigLogVO configVo = new ApprovalFlowConfigLogVO();
        configVo.setConfig(config);
        configVo.setConfigLogs(configLogs);
        configVo.setNodeVos(nodeVos);
        configVo.setConfigSupers(configSupers);
        configVo.setRoles(roles);
        configVo.setConfigs(list());
        return configVo;
    }
    /**
     * 根据审批流编号查询审批流
     * @param approvalFlowNum 审批流编号
     * @return 审批流
     */
    @Override
    public ApprovalFlowConfig findByNum(String approvalFlowNum){
        ApprovalFlowConfigExample configExample = new ApprovalFlowConfigExample();
        configExample.createCriteria().andNumEqualTo(approvalFlowNum);
        List<ApprovalFlowConfig> configs = configMapper.selectByExample(configExample);
        return configs != null && configs.size() > 0 ? configs.get(0) : null;
    }
    /**
     * 根据审批流编号查询审批流
     * @param alias 审批流编号
     * @return 审批流
     */
    private ApprovalFlowConfig findByAlias(String alias){
        ApprovalFlowConfigExample configExample = new ApprovalFlowConfigExample();
        configExample.createCriteria().andAliasEqualTo(alias);
        List<ApprovalFlowConfig> configs = configMapper.selectByExample(configExample);
        return configs != null && configs.size() > 0 ? configs.get(0) : null;
    }

    /**
     * 编辑审批流
     * @param configVO 审批流信息
     */
    @Override
    public void edit(ApprovalFlowConfigVO configVO) {
        if (StringUtils.isEmpty(configVO.getNum())) {
            throw new RuntimeException("审批流编号不能为空");
        }
        ApprovalFlowConfig config = findByNum(configVO.getNum());
        if (config == null) {
            throw new RuntimeException("数据无效");
        }
        config.setVersion(config.getVersion() + 1);
        config.setUpdateUserId(configVO.getCreateUserId());
        config.setUpdateTime(new Date());
        config.setName(configVO.getName());
        config.setH5Link(configVO.getH5Link());
        config.setH5Resume(configVO.getH5Resume());
        config.setType(configVO.getType());
        config.setAlias(configVO.getAlias());
        config.setSort(configVO.getSort());
        updateByPrimaryKey(config);
        configLogService.create(config, configVO.getNodeVos());
    }

    /**
     * 保存审批流
     * @param config 审批流
     */
    private void updateByPrimaryKey(ApprovalFlowConfig config){
        configMapper.updateByPrimaryKey(config);
    }

    /**
     * 添加审批流
     * @param configVO 审批流信息
     */
    @Override
    public void add(ApprovalFlowConfigVO configVO) {
        if (StringUtils.isEmpty(configVO.getAlias())) {
            throw new RuntimeException("审批流别名不能为空");
        }
        ApprovalFlowConfig config = findByAlias(configVO.getAlias());
        if (config != null) {
            throw new RuntimeException("审批流别名已经存在");
        }
        config = new ApprovalFlowConfig();
        config.setAlias(configVO.getAlias());
        config.setCreateUserId(configVO.getCreateUserId());
        config.setUpdateUserId(configVO.getCreateUserId());
        config.setName(configVO.getName());
        config.setH5Link(configVO.getH5Link());
        config.setH5Resume(configVO.getH5Resume());
        config.setType(configVO.getType());
        config.setSort(configVO.getSort());
        create(config);
        configLogService.create(config, configVO.getNodeVos());
    }

    @Override
    public List<ApprovalFlowOrderVO> order() {
        List<ApprovalFlowConfig> configs = list();
        List<ApprovalFlowConfigSuper> configSupers = configSuperService.findAllUsable();
        List<ApprovalFlowOrderVO> orderVOs = new ArrayList<>(configs.size());
        for (ApprovalFlowConfig config : configs){
            ApprovalFlowOrderVO orderVO = new ApprovalFlowOrderVO();
            orderVO.setConfig(config);
            List<ApprovalFlowConfigSuper> configSuperList = new ArrayList<>();
            orderVO.setConfigSupers(configSuperList);

            if (configSupers != null) {
                for (ApprovalFlowConfigSuper configSuper : configSupers) {
                    if (config.getAlias().equals(configSuper.getConfigAlias())) {
                        configSuperList.add(configSuper);
                    }
                }
            }
            orderVOs.add(orderVO);
        }
        return orderVOs;
    }

    @Override
    public void editOrder(List<ApprovalFlowOrderVO> orderVOs) {
        configSuperService.create(orderVOs);
    }

    /**
     * 创建审批流配置，填充默认配置
     * @param config 审批流信息
     */
    private void create(ApprovalFlowConfig config) {
        config.setVersion(1);
        config.setNum(UniqueCodeGenerator.AF_CONFIG.getCode());
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());
        insert(config);
    }

    /**
     * 插入单个配置
     * @param config 审批流
     */
    private void insert(ApprovalFlowConfig config) {
        configMapper.insertSelective(config);
    }

    /**
     * 根据审批流编号删除审批流
     * @param approvalFlowNum 审批流编号
     */
    @Override
    public void delete(String approvalFlowNum) {
        ApprovalFlowConfigExample configExample = new ApprovalFlowConfigExample();
        configExample.createCriteria().andNumEqualTo(approvalFlowNum);
        configMapper.deleteByExample(configExample);

        configLogService.deleteByApprovalFlowNum(approvalFlowNum);
    }

    @Override
    public ApprovalSequenceVO findApprovalSequence(String companyNo, Integer scheduleSort) {
        ApprovalSequenceVO approvalSequence = new ApprovalSequenceVO();
        List<UserRoleSet> roles = roleService.findAll();
        approvalSequence.setRoles(roles);

        ApprovalFlowConfig config = findByAlias(AFAlias.CHECK_APPLICATION.name);
        if (config != null) {
            ApprovalFlowConfigLog configLog = configLogService.findByConfigNumAndVersion(config.getNum(), config.getVersion());
            List<ApprovalFlowNode> nodes = nodeService.findByConfigLogNum(configLog.getNum());
            List<ApprovalFlowScheduleNodeRole> scheduleNodeRoleList= new ArrayList<>();
            for (ApprovalFlowNode node : nodes) {
                List<ApprovalFlowScheduleNodeRole> scheduleNodeRoles = scheduleNodeRoleService.findLastVersionByNodeNumAndCompanyNoAndScheduleSort(node.getNum(), companyNo, scheduleSort);
                if (scheduleNodeRoles != null && scheduleNodeRoles.size() > 0) {
                    scheduleNodeRoleList.addAll(scheduleNodeRoles);
                }
            }
            // 判断公司是否对当前项目节点配置过，如果没有，走相应公司默认配置
            List<ApprovalFlowNodeRoleVO> nodeRoleVOs;
            if (scheduleNodeRoleList.size() > 0){
                nodeRoleVOs = new ArrayList<>(scheduleNodeRoleList.size());
                for (ApprovalFlowScheduleNodeRole scheduleNodeRole : scheduleNodeRoleList) {
                    ApprovalFlowNodeRoleVO nodeRoleVO = getNodeRole(roles, scheduleNodeRole.getRoleId(), scheduleNodeRole.getNodeNum());
                    nodeRoleVOs.add(nodeRoleVO);
                }
            } else {
                List<ApprovalFlowNodeRole> nodeRoleList = new ArrayList<>(nodes.size());
                for (ApprovalFlowNode node : nodes) {
                    List<ApprovalFlowNodeRole> nodeRoles = nodeRoleService.findSendRoleByNodeNum(node.getNum());
                    nodeRoleList.addAll(nodeRoles);
                }
                nodeRoleVOs = new ArrayList<>(nodeRoleList.size());
                for (ApprovalFlowNodeRole nodeRole : nodeRoleList) {
                    ApprovalFlowNodeRoleVO nodeRoleVO = getNodeRole(roles, nodeRole.getRoleId(), nodeRole.getNodeNum());
                    nodeRoleVOs.add(nodeRoleVO);
                }
            }
            approvalSequence.setNodeRoles(nodeRoleVOs);
        } else {
            LOGGER.error("未查询到审批申请审批流，configAlias:{}", AFAlias.CHECK_APPLICATION.name);
            throw new RuntimeException();
        }

        return approvalSequence;
    }

    /**
     * 根据角色Id遍历所有角色信息，使用遍历到的角色与节点编号填充节点角色信息
     * @param roles 所有角色信息
     * @param roleId 角色Id
     * @param nodeNum 节点编号
     * @return 节点角色信息
     */
    private ApprovalFlowNodeRoleVO getNodeRole(List<UserRoleSet> roles, String roleId, String nodeNum) {
        ApprovalFlowNodeRoleVO nodeRoleVO = new ApprovalFlowNodeRoleVO();
        nodeRoleVO.setNodeNum(nodeNum);
        for (UserRoleSet role : roles) {
            if (roleId.equals(role.getRoleCode())) {
                nodeRoleVO.setRole(role);
                break;
            }
        }
        if (nodeRoleVO.getRole() == null) {
            LOGGER.error("未查询到角色信息roleId：{}", roleId);
            throw new RuntimeException();
        }
        return nodeRoleVO;
    }

    @Override
    public void saveApprovalSequence(String companyNo, Integer scheduleSort, Integer scheduleVersion, ApprovalSequenceVO approvalSequence) {
        ApprovalFlowConfig config = findByAlias(AFAlias.CHECK_APPLICATION.name);
        List<ApprovalFlowNode> nodes;
        if (config != null) {
            ApprovalFlowConfigLog configLog = configLogService.findByConfigNumAndVersion(config.getNum(), config.getVersion());
            nodes = nodeService.findByConfigLogNum(configLog.getNum());
            scheduleNodeRoleService.create(nodes, approvalSequence.getNodeRoles(), companyNo, scheduleSort, scheduleVersion);
        } else {
            LOGGER.error("未查询到审批申请审批流");
            throw new RuntimeException();
        }
    }

    @Override
    public void clearConfig() {

    }
}
