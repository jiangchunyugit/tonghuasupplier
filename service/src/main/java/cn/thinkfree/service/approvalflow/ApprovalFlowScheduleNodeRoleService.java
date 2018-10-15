package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRole;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.ApprovalFlowNodeVO;

import java.util.List;

/**
 * @ClassName ApprovalFlowScheduleNodeRoleService
 * @Description 项目节点与审批人员顺序
 * @Author song
 * @Data 2018/10/13 17:31
 * @Version 1.0
 */
public interface ApprovalFlowScheduleNodeRoleService {

    /**
     * 根据节点编号查询最大版本的项目节点角色顺序
     * @param nodeNum 审批节点编号
     * @param scheduleSort 项目节点编号
     * @return 项目节点角色顺序
     */
    List<ApprovalFlowScheduleNodeRole> findLastVersionByNodeNumAndScheduleSort(String nodeNum, Integer scheduleSort);

    /**
     * 创建审批顺序
     * @param nodes 节点顺序
     * @param roles 审批顺序
     * @param scheduleSort 项目节点编号
     */
    void create(List<? extends ApprovalFlowNode> nodes, List<List<UserRoleSet>> roles, Integer scheduleSort);

    /**
     * 更新审批顺序
     * @param nodes 节点顺序
     * @param roles 审批顺序
     * @param scheduleSort 项目节点编号
     */
    void update(List<? extends ApprovalFlowNode> nodes, List<List<UserRoleSet>> roles, Integer scheduleSort);

}
