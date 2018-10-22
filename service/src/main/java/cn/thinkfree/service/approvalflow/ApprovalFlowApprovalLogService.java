package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowApprovalLog;

import java.util.List;

/**
 * 审批流审批日志
 *
 * @author song
 * @version 1.0
 * @date 2018/10/17 17:00
 */
public interface ApprovalFlowApprovalLogService {

    /**
     * 根据审批流实例编号查询有效审批流日志
     * @param instanceNum 审批流实例编号
     * @return 审批流日志
     */
    List<ApprovalFlowApprovalLog> findByInstanceNum(String instanceNum);

    /**
     * 创建审批日志
     * @param instanceNum 审批流实例编号
     * @param nodeNum 审批流配置节点编号
     * @param userId 审批人
     * @param roleId 审批人角色
     * @param optionNum 操作项编号
     * @param remark 审批提交备注
     * @return 审批流日志
     */
    ApprovalFlowApprovalLog create(String instanceNum, String nodeNum, String userId, String roleId, String optionNum, String remark);

    /**
     * 更新数据是否有效
     * @param instanceNum 审批流实例编码
     * @param nodeNums 审批节点编号
     */
    void updateIsInvalidByInstanceNumAndNodeNums(String instanceNum, List<String> nodeNums);
}
