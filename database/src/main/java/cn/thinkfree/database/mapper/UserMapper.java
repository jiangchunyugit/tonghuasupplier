package cn.thinkfree.database.mapper;

import cn.thinkfree.database.model.User;

public interface UserMapper {
    User selectByPrimaryKey(Object pk);
    void insertSelective(User user);
}
