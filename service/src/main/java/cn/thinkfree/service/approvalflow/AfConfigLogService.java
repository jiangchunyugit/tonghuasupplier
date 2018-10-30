package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfConfig;

/**
 * 审批流配置记录服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 17:27
 */
public interface AfConfigLogService {
    /**
     * 创建审批流配置记录
     * @param config 审批流配置
     * @param configLogNo 审批流配置记录编号
     * @param createUserId 创建用户id
     */
    void create(AfConfig config, String configLogNo, String createUserId);
}
