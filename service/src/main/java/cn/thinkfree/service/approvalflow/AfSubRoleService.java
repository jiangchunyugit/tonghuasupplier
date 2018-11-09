package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfSubRole;
import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * 订阅角色服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
public interface AfSubRoleService {
    /**
     * 根据审批流配置方案编号查询订阅角色
     * @param configSchemeNo 审批流配置方案编号
     * @return 订阅角色
     */
    List<AfSubRole> findByConfigSchemeNo(String configSchemeNo);

    /**
     * 根据审批流配置方案编号查询订阅角色
     * @param configSchemeNo 审批流配置方案编号
     * @param allRoles 所有角色信息
     * @return 订阅角色
     */
    List<UserRoleSet> findByConfigSchemeNo(String configSchemeNo, List<UserRoleSet> allRoles);

    /**
     * 创建订阅角色
     * @param configSchemeNo 审批流配置方案编号
     * @param roles 订阅角色信息
     */
    void create(String configSchemeNo, List<UserRoleSet> roles);

}
