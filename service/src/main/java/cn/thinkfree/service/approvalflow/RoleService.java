package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

public interface RoleService {
    List<UserRoleSet> findAll();
}
