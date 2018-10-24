package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRole;
import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * 项目节点与审批人员顺序
 * @author song
 * @date 2018/10/13 17:31
 * @version 1.0
 */
public interface ApprovalFlowScheduleNodeRoleService {

    /**
     * 根据节点编号查询最大版本的项目节点角色顺序
     * @param nodeNum 审批节点编号
     * @param companyNo 公司编号
     * @param scheduleSort 项目节点编号
     * @return 项目节点角色顺序
     */
    List<ApprovalFlowScheduleNodeRole> findLastVersionByNodeNumAndCompanyNoAndScheduleSort(String nodeNum, String companyNo, Integer scheduleSort);

      /**
     * 创建审批顺序
     * @param nodes 节点顺序
     * @param roles 审批顺序
     * @param companyNo 公司编号
     * @param scheduleSort 项目节点编号
     * @param scheduleVersion 项目节点版本号
     */
    void create(List<? extends ApprovalFlowNode> nodes, List<UserRoleSet> roles, String companyNo, Integer scheduleSort, Integer scheduleVersion);

    /**
     * 根据审批流节点信息、项目排期节点编号与版本查询审批角色顺序
     * @param nodes 审批流节点信息
     * @param companyNo 公司编号
     * @param scheduleSort 项目排期节点编号
     * @param scheduleVersion 项目排期节点版本
     * @return 审批角色顺序
     */
    List<ApprovalFlowScheduleNodeRole> findByNodesAndCompanyNoAndScheduleSortAndVersion(List<? extends ApprovalFlowNode> nodes, String companyNo, Integer scheduleSort, Integer scheduleVersion);

    /**
     * 根据审批流节点信息、项目排期节点编号与版本查询审批角色顺序
     * @param nodeNum 审批流节点信息
     * @param companyNo 公司编号
     * @param scheduleSort 项目排期节点编号
     * @param scheduleVersion 项目排期节点版本
     * @return 审批角色顺序
     */
    List<ApprovalFlowScheduleNodeRole> findByNodeNumAndCompanyNoAndScheduleSortAndVersion(String nodeNum, String companyNo, Integer scheduleSort, Integer scheduleVersion);

}
