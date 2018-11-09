package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfApprovalLog;

import java.util.List;

/**
 * 审批日志服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 10:40
 */
public interface AfApprovalLogService {
    /**
     * 根据编号获取审批日志信息
     * @param approvalLogNo 审批日志编号
     * @return 审批日志信息
     */
    AfApprovalLog findByNo(String approvalLogNo);

    /**
     * 根据审批流实例编号获取审批日志
     * @param instanceNo 审批流实例信息
     * @return 审批日志
     */
    List<AfApprovalLog> findByInstanceNo(String instanceNo);

    /**
     * 根据审批流实例编号、序号获取审批日志
     * @param instanceNo 审批流实例信息
     * @param sort 序号
     * @return 审批流实例信息
     */
    AfApprovalLog findByInstanceNoAndSort(String instanceNo, Integer sort);

    /**
     * 更新审批流日志
     * @param approvalLog 审批流日志
     */
    void updateByPrimaryKey(AfApprovalLog approvalLog);

    /**
     * 根据审批流配置编号、项目编号、用户编号查询审批流日志信息
     * @param configNo 审批流日志
     * @param projectNo 项目编号
     * @param userId 用户编号
     * @return 审批流日志
     */
    List<AfApprovalLog> findByConfigNoAndProjectNoAndUserId(String configNo, String projectNo, String userId);

    /**
     * 根据审批流配置编号、项目编号、用户编号查询审批流日志信息
     * @param configNo 审批流日志
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     * @param userId 用户编号
     * @return 审批流日志
     */
    List<AfApprovalLog> findByConfigNoAndProjectNoAndScheduleSortAndUserId(String configNo, String projectNo, Integer scheduleSort, String userId);

    /**
     * 创建审批日志
     * @param approvalLogs 审批日志
     */
    void create(List<AfApprovalLog> approvalLogs);
}
