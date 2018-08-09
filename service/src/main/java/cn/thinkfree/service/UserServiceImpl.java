package cn.thinkfree.service;

import cn.thinkfree.database.mapper.UserMapper;
import cn.thinkfree.database.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{


    @Autowired
    UserMapper userMapper;


    @Override
    public User findById(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void save(User user) {
        userMapper.insertSelective(user);

    }
}
