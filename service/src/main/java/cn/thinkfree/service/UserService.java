package cn.thinkfree.service;

import cn.thinkfree.database.model.User;

public interface UserService {
    User findById(String id);
    void save(User user);
}
