package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.model.UserRoleSetExample;
import cn.thinkfree.service.approvalflow.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserRoleSetMapper userRoleSetMapper;

    @Override
    public List<UserRoleSet> findAll() {
        return userRoleSetMapper.selectByExample(null);
    }

    @Override
    public List<UserRoleSet> findAllShow() {
        UserRoleSetExample example = new UserRoleSetExample();
        example.createCriteria().andIsShowEqualTo((short) 1);
        return userRoleSetMapper.selectByExample(example);
    }

    @Override
    public UserRoleSet findById(String roleId) {
        UserRoleSetExample example = new UserRoleSetExample();
        example.createCriteria().andRoleCodeEqualTo(roleId);
        List<UserRoleSet> roles = userRoleSetMapper.selectByExample(example);
        return roles != null && roles.size() > 0 ? roles.get(0) : null;
    }
}
