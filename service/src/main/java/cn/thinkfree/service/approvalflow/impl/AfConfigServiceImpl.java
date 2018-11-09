package cn.thinkfree.service.approvalflow.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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


    @Resource
    private AfConfigMapper configMapper;
    @Resource
    private AfSubRoleService subRoleService;
    @Resource
    private RoleService roleService;
    @Resource
    private AfApprovalRoleService approvalRoleService;
    @Resource
    private AfConfigSchemeService configSchemeService;

    @Override
    public AfConfigListVO list(String schemeNo) {
        AfConfigListVO configListVO = new AfConfigListVO();
        List<AfConfigVO> configVOs = new ArrayList<>();
        List<UserRoleSet> roles = roleService.findAll();
        List<AfConfig> configs = findAll();

        List<AfConfigScheme> configSchemes = configSchemeService.findBySchemeNo(schemeNo);
        if (configSchemes != null) {
            for (AfConfigScheme configScheme : configSchemes) {
                AfConfigVO configVO = new AfConfigVO();

                List<UserRoleSet> approvalRoles = approvalRoleService.findByConfigSchemeNo(configScheme.getConfigSchemeNo(), roles);
                List<UserRoleSet> subRoles = subRoleService.findByConfigSchemeNo(configScheme.getConfigSchemeNo(), roles);
                configVO.setSubRoles(subRoles);
                configVO.setSubRoles(approvalRoles);
                configVO.setDescribe(configScheme.getDescribe());

                for (AfConfig config : configs) {
                    if (config.getConfigNo().equals(configScheme.getConfigNo())) {
                        configVO.setName(config.getName());
                        configVO.setConfigNo(config.getConfigNo());
                        break;
                    }
                }

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
        if (configVOs == null || configVOs.isEmpty()) {
            LOGGER.error("审批流配置为空");
            throw new RuntimeException();
        }
        for (AfConfigVO configVO : configVOs) {
            String configSchemeNo = UniqueCodeGenerator.AF_CONFIG_SCHEME.getCode();
            configSchemeService.create(configSchemeNo, configVO.getConfigNo(), configEditVO.getSchemeNo(), configVO.getDescribe(), configEditVO.getUserId());
            approvalRoleService.create(configSchemeNo, configVO.getApprovalRoles());
            subRoleService.create(configSchemeNo, configVO.getSubRoles());
        }
    }
}
