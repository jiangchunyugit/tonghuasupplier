package cn.thinkfree.service.approvalflow;


import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * 角色服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
public interface RoleService {
    /**
     * 获取所有角色信息
     * @return
     */
    List<UserRoleSet> findAll();

    /**
     * 根据角色id查询角色
     * @param roleId 角色id
     * @return 角色信息
     */
    UserRoleSet findById(String roleId);
}
