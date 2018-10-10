package cn.thinkfree.service.approvalFlow;


import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

public interface RoleService {
    List<UserRoleSet> findAll();
}
