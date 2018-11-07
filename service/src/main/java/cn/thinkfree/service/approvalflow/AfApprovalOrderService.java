package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfApprovalOrder;
import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * 审批顺序服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 15:53
 */
public interface AfApprovalOrderService {
    /**
     * 根据审批配置方案编号查询审批顺序
     * @param configSchemeNo 审批配置方案编号
     * @param roles 所有角色信息
     * @return 审批顺序
     */
    List<List<UserRoleSet>> findByConfigSchemeNo(String configSchemeNo, List<UserRoleSet> roles);

    /**
     * 创建审批顺序
     * @param configSchemeNo 审批配置方案编号
     * @param approvalOrders 审批顺序
     */
    void create(String configSchemeNo, List<List<UserRoleSet>> approvalOrders);

    /**
     * 根据审批配置方案编号、角色编号查询审批顺序
     * @param configSchemeNo 审批配置方案编号
     * @param roleId 角色编号
     * @return 审批顺序
     */
    AfApprovalOrder findByConfigSchemeNoAndRoleId(String configSchemeNo, String roleId);

    /**
     * 根据项目编号、审批流配置编号、用户编号查询审批顺序
     * @param projectNo 项目编号
     * @param configNo 审批流配置编号
     * @param userId 用户编号
     * @return 审批顺序
     */
    AfApprovalOrder findByProjectNoAndConfigNoAndUserId(String projectNo, String configNo, String userId);

    /**
     * 根据审批流配置编号、方案编号、角色编号查询审批顺序
     * @param configNo 审批流配置编号
     * @param schemeNo 方案编号
     * @param roleId 角色编号
     * @return 审批顺序
     */
    AfApprovalOrder findByConfigNoAndSchemeNoAndRoleId(String configNo, String schemeNo, String roleId);
}
