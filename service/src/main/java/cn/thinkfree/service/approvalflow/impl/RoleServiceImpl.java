package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.model.UserRoleSetExample;
import cn.thinkfree.service.approvalflow.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Resource
    private UserRoleSetMapper userRoleSetMapper;

    @Override
    public List<UserRoleSet> findAll() {
        return userRoleSetMapper.selectByExample(null);
    }

    @Override
    public UserRoleSet findById(String roleId) {
        UserRoleSetExample example = new UserRoleSetExample();
        example.createCriteria().andRoleCodeEqualTo(roleId);
        List<UserRoleSet> roles = userRoleSetMapper.selectByExample(example);
        return roles != null && roles.size() > 0 ? roles.get(0) : null;
    }
}
