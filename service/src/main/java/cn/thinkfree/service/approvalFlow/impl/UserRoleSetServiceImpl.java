package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.approvalFlow.UserRoleSetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserRoleSetServiceImpl implements UserRoleSetService {

    @Resource
    private UserRoleSetMapper userRoleSetMapper;

    @Override
    public List<UserRoleSet> findAll() {
        return userRoleSetMapper.selectByExample(null);
    }
}
