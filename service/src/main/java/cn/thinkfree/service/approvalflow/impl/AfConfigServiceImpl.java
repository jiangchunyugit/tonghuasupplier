package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.constants.AfConfigs;
import cn.thinkfree.core.constants.AfConstants;
import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfConfigMapper;
import cn.thinkfree.database.model.AfConfig;
import cn.thinkfree.database.model.AfConfigExample;
import cn.thinkfree.database.model.AfConfigScheme;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.AfConfigEditVO;
import cn.thinkfree.database.vo.AfConfigListVO;
import cn.thinkfree.database.vo.AfConfigVO;
import cn.thinkfree.service.approvalflow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.thinkfree.core.constants.AfConstants.CONFIG_TYPE_COUNT;

/**
 * 审批流配置服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 16:14
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfConfigServiceImpl implements AfConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfConfigServiceImpl.class);


    @Autowired
    private AfConfigMapper configMapper;
    @Autowired
    private AfSubRoleService subRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AfApprovalRoleService approvalRoleService;
    @Autowired
    private AfConfigSchemeService configSchemeService;

    @Override
    public AfConfigListVO list(String schemeNo) {
        AfConfigListVO configListVO = new AfConfigListVO();
        List<AfConfigVO> configVOs = new ArrayList<>();
        List<UserRoleSet> roles = roleService.findAllShow();
        List<AfConfig> configs = findAll();

        List<AfConfigScheme> configSchemes = configSchemeService.findBySchemeNo(schemeNo);
        if (configSchemes != null) {
            for (AfConfigScheme configScheme : configSchemes) {
                AfConfigVO configVO = new AfConfigVO();

                List<UserRoleSet> approvalRoles = approvalRoleService.findByConfigSchemeNo(configScheme.getConfigSchemeNo(), roles);
                configVO.setApprovalRoles(approvalRoles);

                List<UserRoleSet> subRoles = subRoleService.findByConfigSchemeNo(configScheme.getConfigSchemeNo(), roles);
                configVO.setSubRoles(subRoles);

                configVO.setDescribe(configScheme.getDescribe());
                configVO.setConfigNo(configScheme.getConfigNo());

                configVOs.add(configVO);
            }
        }

        configListVO.setConfigs(configs);
        configListVO.setConfigSchemes(configVOs);
        configListVO.setRoles(roles);
        return configListVO;
    }

    /**
     * 查询所有审批流配置信息
     * @return 审批流配置信息
     */
    private List<AfConfig> findAll() {
        AfConfigExample example = new AfConfigExample();
        return configMapper.selectByExample(example);
    }

    @Override
    public AfConfig findByNo(String configNo) {
        AfConfigExample example = new AfConfigExample();
        example.createCriteria().andConfigNoEqualTo(configNo);
        List<AfConfig> configs = configMapper.selectByExample(example);
        return configs != null && configs.size() > 0 ? configs.get(0) : null;
    }

    @Override
    public AfConfigVO detail(String configNo, String schemeNo) {
        AfConfigVO configVO = new AfConfigVO();

        AfConfig config = findByNo(configNo);
        if (config == null) {
            LOGGER.error("未查询到审批流配置信息，configNo：{}", configNo);
            throw new RuntimeException();
        }
        AfConfigScheme configPlan = configSchemeService.findByConfigNoAndSchemeNo(configNo, schemeNo);

        if (configPlan != null) {
            List<UserRoleSet> roles = roleService.findAll();
            List<UserRoleSet> approvalRoles = approvalRoleService.findByConfigSchemeNo(configPlan.getConfigSchemeNo(), roles);
            List<UserRoleSet> subRoles = subRoleService.findByConfigSchemeNo(configPlan.getConfigSchemeNo(), roles);
            configVO.setApprovalRoles(approvalRoles);
            configVO.setSubRoles(subRoles);
            configVO.setDescribe(configPlan.getDescribe());
        }

        configVO.setConfigNo(config.getConfigNo());
        configVO.setName(config.getName());
        return configVO;
    }

    @Override
    public void edit(AfConfigEditVO configEditVO) {
        List<AfConfigVO> configVOs = configEditVO.getConfigVOs();

        verifyApprovalConfig(configVOs);

        for (AfConfigVO configVO : configVOs) {
            String configSchemeNo = UniqueCodeGenerator.AF_CONFIG_SCHEME.getCode();
            configSchemeService.create(configEditVO.getSchemeNo(), configEditVO.getUserId(), configSchemeNo, configVO);
            approvalRoleService.create(configSchemeNo, configVO.getApprovalRoles());
            subRoleService.create(configSchemeNo, configVO.getSubRoles());
        }
    }

    private void verifyApprovalConfig(List<AfConfigVO> configVOs) {
        if (configVOs == null || configVOs.isEmpty()) {
            LOGGER.error("审批流配置为空");
            throw new RuntimeException();
        }

        Map<String, Object> typeCount = new HashMap<>(10);
        Object object = new Object();
        for (AfConfigVO configVO : configVOs) {
            typeCount.put(configVO.getConfigNo(), object);

            List<UserRoleSet> approvalRoles = configVO.getApprovalRoles();
            if (approvalRoles == null || approvalRoles.isEmpty()) {
                LOGGER.error("未设置审批角色");
                throw new RuntimeException();
            }

            for (int i = 0; i < approvalRoles.size(); i++) {
                for (int j = i + 1; j < approvalRoles.size(); j++) {
                    if (approvalRoles.get(i).getRoleCode().equals(approvalRoles.get(j).getRoleCode())) {
                        LOGGER.error("审批列表包含相同角色");
                        throw new RuntimeException();
                    }
                }
            }

            List<UserRoleSet> subRoles = configVO.getSubRoles();
            if (subRoles != null) {
                for (UserRoleSet subRole : subRoles) {
                    if (subRole.getRoleCode().equals(approvalRoles.get(0).getRoleCode())) {
                        LOGGER.error("订阅角色包含发起角色");
                        throw new RuntimeException();
                    }
                }

                for (int i = 0; i < subRoles.size(); i++) {
                    for (int j = i + 1; j < subRoles.size(); j++) {
                        if (subRoles.get(i).getRoleCode().equals(subRoles.get(j).getRoleCode())) {
                            LOGGER.error("订阅列表包含相同角色");
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
        if (typeCount.size() < CONFIG_TYPE_COUNT) {
            LOGGER.error("审批流种类配置不全");
            throw new RuntimeException();
        }
    }

    @Override
    public List<String> getConfigNosByApprovalType(String approvalType) {
        List<String> configNos = new ArrayList<>();
        if (AfConstants.APPROVAL_TYPE_SCHEDULE_APPROVAL.equals(approvalType)) {
            configNos.add(AfConfigs.START_APPLICATION.configNo);
            configNos.add(AfConfigs.START_REPORT.configNo);
            configNos.add(AfConfigs.CHECK_APPLICATION.configNo);
            configNos.add(AfConfigs.CHECK_REPORT.configNo);
            configNos.add(AfConfigs.COMPLETE_APPLICATION.configNo);
        } else if (AfConstants.APPROVAL_TYPE_CONSTRUCTION_CHANGE.equals(approvalType)) {
            configNos.add(AfConfigs.CHANGE_COMPLETE.configNo);
            configNos.add(AfConfigs.CHANGE_ORDER.configNo);
        } else if (AfConstants.APPROVAL_TYPE_PROBLEM_RECTIFICATION.equals(approvalType)) {
            configNos.add(AfConfigs.PROBLEM_RECTIFICATION.configNo);
            configNos.add(AfConfigs.RECTIFICATION_COMPLETE.configNo);
        } else if (AfConstants.APPROVAL_TYPE_DELAY_VERIFY.equals(approvalType)) {
            configNos.add(AfConfigs.DELAY_ORDER.configNo);
        } else {
            LOGGER.error("错误的审批类型，approvalType:{}", approvalType);
            throw new RuntimeException();
        }
        return configNos;
    }
}
