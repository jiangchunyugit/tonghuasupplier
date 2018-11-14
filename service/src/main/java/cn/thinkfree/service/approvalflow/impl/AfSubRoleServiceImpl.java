package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.database.mapper.AfSubRoleMapper;
import cn.thinkfree.database.model.AfSubRole;
import cn.thinkfree.database.model.AfSubRoleExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.approvalflow.AfSubRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 订阅角色服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfSubRoleServiceImpl implements AfSubRoleService {

    private static final MyLogger LOGGER = new MyLogger(AfSubRoleServiceImpl.class);


    @Autowired
    private AfSubRoleMapper subRoleMapper;

    @Override
    public List<AfSubRole> findByConfigSchemeNo(String configSchemeNo) {
        AfSubRoleExample example = new AfSubRoleExample();
        example.createCriteria().andConfigSchemeNoEqualTo(configSchemeNo);
        return subRoleMapper.selectByExample(example);
    }

    @Override
    public List<UserRoleSet> findByConfigSchemeNo(String configSchemeNo, List<UserRoleSet> allRoles) {
        List<AfSubRole> subRoles = findByConfigSchemeNo(configSchemeNo);
        return getRoles(subRoles, allRoles);
    }

    /**
     * 根据订阅角色信息、所有角色信息获取详细审批角色信息
     * @param subRoles 订阅角色信息
     * @param allRoles 所有角色信息
     * @return 详细订阅角色信息
     */
    private List<UserRoleSet> getRoles(List<AfSubRole> subRoles, List<UserRoleSet> allRoles) {
        List<UserRoleSet> roles = new ArrayList<>();
        if (subRoles != null) {
            for (AfSubRole subRole : subRoles) {
                UserRoleSet role = null;
                for (UserRoleSet record : allRoles) {
                    if (record.getRoleCode().equals(subRole.getRoleId())) {
                        role = record;
                        break;
                    }
                }
                if (role == null) {
                    LOGGER.error("未获取到角色信息，roleId：{}", subRole.getRoleId());
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
            AfSubRole subRole;
            for (int index = 0; index < roles.size(); index++){
                subRole = new AfSubRole();
                subRole.setConfigSchemeNo(configSchemeNo);
                subRole.setRoleId(roles.get(index).getRoleCode());
                insert(subRole);
            }
        }
    }
    private void insert(AfSubRole subRole) {
        subRoleMapper.insertSelective(subRole);
    }
}
