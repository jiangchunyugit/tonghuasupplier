package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfConfigMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.AfConfigEditVO;
import cn.thinkfree.database.vo.AfConfigVO;
import cn.thinkfree.service.approvalflow.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private AfApprovalOrderService approvalOrderService;
    @Resource
    private AfConfigPlanService configPlanService;
    @Resource
    private AfApprovalRoleService approvalRoleService;

    @Override
    public List<AfConfigVO> list(String planNo) {
        List<AfConfigVO> configVOs = new ArrayList<>();
        List<UserRoleSet> roles = roleService.findAll();
        List<AfConfig> configs = findAll();
        if (configs != null) {
            for (AfConfig config : configs) {
                AfConfigVO configVO = new AfConfigVO();
                if (StringUtils.isNotEmpty(planNo)) {
                    List<AfConfigPlan> configPlans = configPlanService.findByPlanNo(planNo);
                    if (configPlans != null) {
                        for (AfConfigPlan configPlan : configPlans) {
                            if (config.getConfigNo().equals(configPlan.getConfigNo())) {
                                List<List<UserRoleSet>> approvalOrders = approvalOrderService.findByConfigPlanNo(configPlan.getConfigPlanNo(), roles);
                                List<UserRoleSet> subRoles = subRoleService.findByConfigPlanNo(configPlan.getConfigPlanNo(), roles);
                                configVO.setSubRoles(subRoles);
                                configVO.setApprovalOrders(approvalOrders);
                                configVO.setDescribe(configPlan.getDescribe());
                                break;
                            }
                        }
                    }
                }
                configVO.setConfigNo(config.getConfigNo());
                configVO.setName(config.getName());
                configVOs.add(configVO);
            }
        }
        return configVOs;
    }


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
    public AfConfigVO detail(String configNo, String planNo) {
        AfConfigVO configVO = new AfConfigVO();

        AfConfig config = findByNo(configNo);
        if (config == null) {
            // TODO
            throw new RuntimeException();
        }
        AfConfigPlan configPlan = configPlanService.findByConfigNoAndPlanNo(configNo, planNo);

        if (configPlan != null) {
            List<UserRoleSet> roles = roleService.findAll();
            List<List<UserRoleSet>> approvalOrders = approvalOrderService.findByConfigPlanNo(configPlan.getConfigPlanNo(), roles);
            List<UserRoleSet> subRoles = subRoleService.findByConfigPlanNo(configPlan.getConfigPlanNo(), roles);
            configVO.setApprovalOrders(approvalOrders);
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
        if (configVOs == null || configVOs.size() != 10) {
            // TODO
        }
        for (AfConfigVO configVO : configVOs) {
            String configPlanNo = UniqueCodeGenerator.AF_CONFIG_PLAN.getCode();
            configPlanService.create(configPlanNo, configVO.getConfigNo(), configEditVO.getPlanNo(),configVO.getDescribe(), configEditVO.getUserId());
            approvalOrderService.create(configPlanNo, configVO.getConfigNo(), configVO.getApprovalOrders());
            subRoleService.create(configPlanNo, configVO.getSubRoles());
        }
    }

    @Override
    public List<UserRoleSet> findApprovalOrder(String configNo, String planNo, String roleId, List<UserRoleSet> allRoles) {
        List<UserRoleSet> approvalRoles = null;
        AfConfigPlan configPlan = configPlanService.findByConfigNoAndPlanNo(configNo, planNo);
        if (configPlan != null) {
            AfApprovalOrder approvalOrder = approvalOrderService.findByConfigPlanNoAndRoleId(configPlan.getConfigPlanNo(), roleId);
            if (approvalOrder != null) {
                approvalRoles = approvalRoleService.findByApprovalOrderNo(approvalOrder.getApprovalOrderNo(), allRoles);
            }
        }
        return approvalRoles;
    }
}
