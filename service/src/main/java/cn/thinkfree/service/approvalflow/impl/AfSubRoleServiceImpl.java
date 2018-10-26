package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfSubRoleMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.approvalflow.AfSubRoleService;
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
public class AfSubRoleServiceImpl implements AfSubRoleService {

    @Resource
    private AfSubRoleMapper subRoleMapper;

    @Override
    public List<AfSubRole> findByConfigLogNo(String configLogNo) {
        AfSubRoleExample example = new AfSubRoleExample();
        example.createCriteria().andConfigLogNoEqualTo(configLogNo);
        return subRoleMapper.selectByExample(example);
    }

    @Override
    public List<UserRoleSet> findByConfigLogNo(String configLogNo, List<UserRoleSet> allRoles) {
        List<AfSubRole> subRoles = findByConfigLogNo(configLogNo);
        return getRoles(subRoles, allRoles);
    }

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
    public void create(String configLogNo, List<UserRoleSet> roles) {
        if (roles != null) {
            AfSubRole subRole;
            for (int index = 0; index < roles.size(); index++){
                subRole = new AfSubRole();
                subRole.setConfigLogNo(configLogNo);
                subRole.setRoleId(roles.get(index).getRoleCode());
                insert(subRole);
            }
        }
    }
    private void insert(AfSubRole subRole) {
        subRoleMapper.insertSelective(subRole);
    }
}
