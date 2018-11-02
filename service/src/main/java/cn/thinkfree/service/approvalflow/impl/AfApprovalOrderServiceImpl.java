package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.AfApprovalOrderMapper;
import cn.thinkfree.database.model.AfApprovalOrder;
import cn.thinkfree.database.model.AfApprovalOrderExample;
import cn.thinkfree.database.model.AfConfigScheme;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.approvalflow.AfApprovalOrderService;
import cn.thinkfree.service.approvalflow.AfApprovalRoleService;
import cn.thinkfree.service.approvalflow.AfConfigSchemeService;
import cn.thinkfree.service.neworder.NewOrderUserService;
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
 * @date 2018/10/26 15:53
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfApprovalOrderServiceImpl implements AfApprovalOrderService {

    @Resource
    private AfApprovalOrderMapper approvalOrderMapper;
    @Resource
    private AfApprovalRoleService approvalRoleService;
    @Resource
    private AfConfigSchemeService configPlanService;
    @Resource
    private NewOrderUserService orderUserService;

    @Override
    public List<List<UserRoleSet>> findByConfigSchemeNo(String configSchemeNo, List<UserRoleSet> allRoles) {
        List<List<UserRoleSet>> roleList = new ArrayList<>();
        AfApprovalOrderExample example = new AfApprovalOrderExample();
        example.createCriteria().andConfigSchemeNoEqualTo(configSchemeNo);
        List<AfApprovalOrder> approvalOrders = approvalOrderMapper.selectByExample(example);
        if (approvalOrders != null) {
            for (AfApprovalOrder approvalOrder : approvalOrders) {
                List<UserRoleSet> roles = approvalRoleService.findByApprovalOrderNo(approvalOrder.getApprovalOrderNo(), allRoles);
                roleList.add(roles);
            }
        }
        return roleList;
    }

    @Override
    public void create(String configSchemeNo, String configNo, List<List<UserRoleSet>> approvalOrders) {
        if (approvalOrders != null) {
            AfApprovalOrder approvalOrder;
            for (List<UserRoleSet> roles : approvalOrders) {
                if (roles.size() > 0) {
                    approvalOrder = new AfApprovalOrder();
                    approvalOrder.setId(null);
                    approvalOrder.setConfigNo(configNo);
                    approvalOrder.setConfigSchemeNo(configSchemeNo);
                    approvalOrder.setApprovalOrderNo(UniqueCodeGenerator.AF_APPROVAL_ORDER.getCode());
                    approvalOrder.setFirstRoleId(roles.get(0).getRoleCode());

                    approvalRoleService.create(approvalOrder.getApprovalOrderNo(), roles);
                    insert(approvalOrder);
                }
            }
        }
    }

    private void insert(AfApprovalOrder approvalOrder) {
        approvalOrderMapper.insertSelective(approvalOrder);
    }

    @Override
    public AfApprovalOrder findByConfigSchemeNoAndRoleId(String configSchemeNo, String roleId) {
        AfApprovalOrderExample example = new AfApprovalOrderExample();
        example.createCriteria().andConfigSchemeNoEqualTo(configSchemeNo).andFirstRoleIdEqualTo(roleId);
        List<AfApprovalOrder> approvalOrders = approvalOrderMapper.selectByExample(example);
        return approvalOrders != null && approvalOrders.size() > 0 ? approvalOrders.get(0) : null;
    }

    @Override
    public AfApprovalOrder findByProjectNoAndConfigNoAndUserId(String projectNo, String configNo, String userId) {
        String schemeNo = getSchemeNo(projectNo);
        String roleId = orderUserService.findRoleIdByProjectNoAndUserId(projectNo, userId);

        AfConfigScheme configScheme = configPlanService.findByConfigNoAndSchemeNo(configNo, schemeNo);
        if (configScheme != null) {
            return findByConfigSchemeNoAndRoleId(configScheme.getConfigSchemeNo(), roleId);
        }
        return null;
    }

    private String getSchemeNo(String projectNo){
        // TODO
        return "";
    }

    @Override
    public AfApprovalOrder findByConfigNoAndSchemeNoAndRoleId(String configNo, String schemeNo, String roleId) {
        AfConfigScheme configScheme = configPlanService.findByConfigNoAndSchemeNo(configNo, schemeNo);
        if (configScheme != null) {
            return findByConfigSchemeNoAndRoleId(configScheme.getConfigSchemeNo(), roleId);
        }
        return null;
    }
}
