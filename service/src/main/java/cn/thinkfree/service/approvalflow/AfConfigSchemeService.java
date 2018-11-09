package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfConfigScheme;

import java.util.List;

/**
 * 审批流配置方案关系服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/30 17:01
 */
public interface AfConfigSchemeService {
    /**
     * 根据方案编号查询审批流配置方案
     * @param schemeNo 方案编号
     * @return 审批流配置方案
     */
    List<AfConfigScheme> findBySchemeNo(String schemeNo);

    /**
     * 根据审批流配置编号、方案编号查询审批流配置方案
     * @param configNo 审批流配置编号
     * @param schemeNo 方案编号
     * @return 审批配置方案
     */
    AfConfigScheme findByConfigNoAndSchemeNo(String configNo, String schemeNo);

    /**
     * 创建审批流配置方案
     * @param configSchemeNo 审批流配置方案编号
     * @param configNo 审批流配置编号
     * @param schemeNo 方案编号
     * @param describe 描述
     * @param userId 用户编号
     */
    void create(String configSchemeNo, String configNo, String schemeNo, String describe, String userId);

    String findByProjectNoAndConfigNoAndUserId(String projectNo, String configNo, String userId);

    AfConfigScheme findByConfigNoAndSchemeNoAndRoleId(String configNo, String schemeNo, String roleId);

}
