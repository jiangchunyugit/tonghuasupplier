package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfConfigMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.AfConfigVO;
import cn.thinkfree.database.vo.AfPlanVO;
import cn.thinkfree.service.approvalflow.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 16:14
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfConfigServiceImpl implements AfConfigService {

    @Resource
    private AfConfigMapper configMapper;
    @Resource
    private AfSubRoleService subRoleService;
    @Resource
    private RoleService roleService;
    @Resource
    private AfConfigLogService configLogService;
    @Resource
    private AfPlanService planService;

    @Override
    public List<AfConfigVO> list() {
        List<AfConfigVO> configVOs = new ArrayList<>();
        List<AfConfig> configs = findAll();
        if (configs != null) {
            List<UserRoleSet> roles = roleService.findAll();
            for (AfConfig config : configs) {
                List<UserRoleSet> subRoles = subRoleService.findByConfigLogNo(config.getConfigLogNo(), roles);
                List<AfPlanVO> plans = planService.findByConfigLogNo(config.getConfigLogNo(), roles);
                AfConfigVO configVO = new AfConfigVO();
                configVO.setConfig(config);
                configVO.setSubRoles(subRoles);
                configVO.setPlans(plans);

                configVOs.add(configVO);
            }
        }
        return configVOs;
    }


    private List<AfConfig> findAll() {
        AfConfigExample example = new AfConfigExample();
        example.createCriteria().andUsableEqualTo(1);
        return configMapper.selectByExample(example);
    }

    @Override
    public AfConfig findByNo(String configNo) {
        AfConfigExample example = new AfConfigExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andUsableEqualTo(1);
        List<AfConfig> configs = configMapper.selectByExample(example);
        return configs != null && configs.size() > 0 ? configs.get(0) : null;
    }

    @Override
    public AfConfigVO detail(String configNo) {
        AfConfig config = findByNo(configNo);
        if (config == null) {
            // TODO
            throw new RuntimeException();
        }
        List<UserRoleSet> roles = roleService.findAll();
        List<AfPlanVO> plans = planService.findByConfigLogNo(config.getConfigNo(), roles);
        List<UserRoleSet> subRoles = subRoleService.findByConfigLogNo(config.getConfigLogNo(), roles);

        AfConfigVO configVO = new AfConfigVO();
        configVO.setConfig(config);
        configVO.setPlans(plans);
        configVO.setSubRoles(subRoles);
        return configVO;
    }

    @Override
    public void add(AfConfigVO configVO) {
        AfConfig config = configVO.getConfig();
        String configLogNo = UniqueCodeGenerator.AF_CONFIG_LOG.getCode();
        config.setConfigLogNo(configLogNo);
        config.setVersion(1);
        config.setCreateTime(new Date());
        config.setConfigNo(UniqueCodeGenerator.AF_CONFIG.getCode());
        config.setUsable(1);

        insert(config);

        configLogService.create(config, configLogNo, config.getCreateUserId());
        planService.create(configLogNo, configVO.getPlans());
        subRoleService.create(configLogNo, configVO.getSubRoles());
    }

    private void insert(AfConfig config) {
        configMapper.insertSelective(config);
    }

    @Override
    public void edit(AfConfigVO configVO) {
        AfConfig config = configVO.getConfig();
        AfConfig record = findByNo(config.getConfigNo());
        if (record == null) {
            // TODO
            throw new RuntimeException();
        }

        String configLogNo = UniqueCodeGenerator.AF_CONFIG_LOG.getCode();
        record.setAlias(config.getAlias());
        record.setName(config.getName());
        record.setUpdateTime(new Date());
        record.setUpdateUserId("");
        record.setConfigLogNo(configLogNo);
        record.setVersion(record.getVersion() + 1);

        AfConfigExample example = new AfConfigExample();
        example.createCriteria().andConfigNoEqualTo(config.getConfigNo());

        configMapper.updateByPrimaryKey(record);

        configLogService.create(record, configLogNo, config.getUpdateUserId());
        planService.create(configLogNo, configVO.getPlans());
        subRoleService.create(configLogNo, configVO.getSubRoles());
    }

    @Override
    public void delete(String configNo) {
        AfConfig config = new AfConfig();
        config.setUsable(0);
        AfConfigExample example = new AfConfigExample();
        example.createCriteria().andConfigNoEqualTo(configNo);
        int row = configMapper.updateByExampleSelective(config, example);
        if (row != 1) {
            // TODO
        }
    }

    @Override
    public AfConfig findByAlias(String alias) {
        AfConfigExample example = new AfConfigExample();
        example.createCriteria().andAliasNotEqualTo(alias).andUsableEqualTo(1);
        List<AfConfig> configs = configMapper.selectByExample(example);
        return configs != null && configs.size() > 0 ? configs.get(0) : null;
    }

    @Override
    public String findConfigNoByAlias(String alias) {
        AfConfig config = findByAlias(alias);
        return config != null ? config.getConfigNo() : null;
    }
}
