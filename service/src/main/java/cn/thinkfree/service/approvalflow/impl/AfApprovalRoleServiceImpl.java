package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.database.mapper.AfApprovalRoleMapper;
import cn.thinkfree.database.model.AfApprovalRole;
import cn.thinkfree.database.model.AfApprovalRoleExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.approvalflow.AfApprovalRoleService;
import cn.thinkfree.service.approvalflow.AfConfigSchemeService;
import cn.thinkfree.service.approvalflow.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批角色服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfApprovalRoleServiceImpl implements AfApprovalRoleService {

    private static final MyLogger LOGGER = new MyLogger(AfApprovalRoleServiceImpl.class);

    @Resource
    private AfApprovalRoleMapper approvalRoleMapper;
    @Resource
    private RoleService roleService;
    @Resource
    private AfConfigSchemeService configSchemeService;

    @Override
    public List<AfApprovalRole> findByConfigSchemeNo(String configSchemeNo) {
        AfApprovalRoleExample example = new AfApprovalRoleExample();
        example.createCriteria().andConfigSchemeNoEqualTo(configSchemeNo);
        example.setOrderByClause("sort asc");
        return approvalRoleMapper.selectByExample(example);
    }

    @Override
    public List<UserRoleSet> findByConfigSchemeNo(String configSchemeNo, List<UserRoleSet> allRoles) {
        List<AfApprovalRole> approvalRoles = findByConfigSchemeNo(configSchemeNo);
        return getRoles(approvalRoles, allRoles);
    }

    /**
     * 根据审批角色信息、所有角色信息获取详细审批角色信息
     * @param approvalRoles 审批角色信息
     * @param allRoles 所有角色信息
     * @return 详细审批角色信息
     */
    private List<UserRoleSet> getRoles(List<AfApprovalRole> approvalRoles, List<UserRoleSet> allRoles) {
        List<UserRoleSet> roles = new ArrayList<>();
        if (approvalRoles != null) {
            for (AfApprovalRole approvalRole : approvalRoles) {
                UserRoleSet role = null;
                for (UserRoleSet record : allRoles) {
                    if (record.getRoleCode().equals(approvalRole.getRoleId())) {
                        role = record;
                        break;
                    }
                }
                if (role == null) {
                    LOGGER.error("获取角色信息出错，roleId:{}", approvalRole.getRoleId());
                    throw new RuntimeException();
                }
                roles.add(role);
            }
        }
        return roles;
    }

    @Override
    public void create(String configSchemeNo, List<UserRoleSet> roles) {
        if (roles != null) {
            AfApprovalRole approvalRole;
            for (int index = 0; index < roles.size(); index++){
                approvalRole = new AfApprovalRole();
                approvalRole.setConfigSchemeNo(configSchemeNo);
                approvalRole.setRoleId(roles.get(index).getRoleCode());
                approvalRole.setSort(index);
                insert(approvalRole);
            }
        }
    }
    private void insert(AfApprovalRole approvalRole) {
        approvalRoleMapper.insertSelective(approvalRole);
    }
}
