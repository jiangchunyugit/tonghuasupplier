package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfConfigMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.AfConfigVO;
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
    private AfApprovalRoleService approvalRoleService;
    @Resource
    private AfPubRoleService pubRoleService;
    @Resource
    private AfSubRoleService subRoleService;
    @Resource
    private AfSubUrlService subUrlService;
    @Resource
    private RoleService roleService;
    @Resource
    private AfConfigLogService configLogService;

    @Override
    public List<AfConfigVO> list() {
        List<AfConfigVO> configVOs = new ArrayList<>();
        List<AfConfig> configs = findAll();
        if (configs != null) {
            List<UserRoleSet> roles = roleService.findAll();
            for (AfConfig config : configs) {
                List<UserRoleSet> approvalRoles = approvalRoleService.findByConfigLogNo(config.getConfigLogNo(), roles);
                List<UserRoleSet> pubRoles = pubRoleService.findByConfigLogNo(config.getConfigLogNo(), roles);
                List<UserRoleSet> subRoles = subRoleService.findByConfigLogNo(config.getConfigLogNo(), roles);
                List<AfSubUrl> subUrls = subUrlService.findByConfigLogNo(config.getConfigLogNo());

                AfConfigVO configVO = new AfConfigVO();
                configVO.setConfig(config);
                configVO.setApprovalRoles(approvalRoles);
                configVO.setPubRoles(pubRoles);
                configVO.setSubRoles(subRoles);
                configVO.setSubUrls(subUrls);

                configVOs.add(configVO);
            }
        }
        return configVOs;
    }


    private List<AfConfig> findAll() {
        return configMapper.selectByExample(null);
    }

    private AfConfig findByNo(String configNo) {
        AfConfigExample example = new AfConfigExample();
        example.createCriteria().andConfigNoEqualTo(configNo);
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
        return detail(config, roles);
    }

    private AfConfigVO detail(AfConfig config, List<UserRoleSet> roles) {
        List<UserRoleSet> approvalRoles = approvalRoleService.findByConfigLogNo(config.getConfigLogNo(), roles);
        List<UserRoleSet> pubRoles = pubRoleService.findByConfigLogNo(config.getConfigLogNo(), roles);
        List<UserRoleSet> subRoles = subRoleService.findByConfigLogNo(config.getConfigLogNo(), roles);
        List<AfSubUrl> subUrls = subUrlService.findByConfigLogNo(config.getConfigLogNo());

        AfConfigVO configVO = new AfConfigVO();
        configVO.setConfig(config);
        configVO.setApprovalRoles(approvalRoles);
        configVO.setPubRoles(pubRoles);
        configVO.setSubRoles(subRoles);
        configVO.setSubUrls(subUrls);
        return configVO;
    }

    @Override
    public void update(AfConfigVO configVO) {
        AfConfig config = configVO.getConfig();
        AfConfig record = findByNo(config.getConfigNo());
        String configLogNo = UniqueCodeGenerator.AF_CONFIG_LOG.getCode();
        record.setAlias(config.getAlias());
        record.setName(config.getName());
        record.setUpdateTime(new Date());
        record.setUpdateUserId("");
        record.setConfigLogNo(configLogNo);
        record.setVersion(record.getVersion() + 1);
        save(record);

        configLogService.create(record, configLogNo);
        approvalRoleService.create(configLogNo, configVO.getApprovalRoles());
        pubRoleService.create(configLogNo, configVO.getPubRoles());
        subRoleService.create(configLogNo, configVO.getSubRoles());
        subUrlService.create(configLogNo, configVO.getSubUrls());
    }

    private void save(AfConfig config) {
        configMapper.updateByPrimaryKey(config);
    }
}
