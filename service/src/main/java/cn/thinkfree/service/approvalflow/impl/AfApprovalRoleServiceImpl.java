package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfApprovalRoleMapper;
import cn.thinkfree.database.model.AfApprovalRole;
import cn.thinkfree.database.model.AfApprovalRoleExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.approvalflow.AfApprovalRoleService;
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
 * @date 2018/10/25 15:52
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfApprovalRoleServiceImpl implements AfApprovalRoleService {

    @Resource
    private AfApprovalRoleMapper approvalRoleMapper;

    @Override
    public List<AfApprovalRole> findByPlanNo(String planNo) {
        AfApprovalRoleExample example = new AfApprovalRoleExample();
        example.createCriteria().andPlanNoEqualTo(planNo);
        example.setOrderByClause("sort asc");
        return approvalRoleMapper.selectByExample(example);
    }

    @Override
    public List<UserRoleSet> findByPlanNo(String planNo, List<UserRoleSet> allRoles) {
        List<AfApprovalRole> approvalRoles = findByPlanNo(planNo);
        return getRoles(approvalRoles, allRoles);
    }

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
                    // TODO
                    throw new RuntimeException();
                } else {
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    @Override
    public void create(String planNo, List<UserRoleSet> roles) {
        if (roles != null) {
            AfApprovalRole approvalRole;
            for (int index = 0; index < roles.size(); index++){
                approvalRole = new AfApprovalRole();
                approvalRole.setPlanNo(planNo);
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
