package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowScheduleNodeRoleMapper;
import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRole;
import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRoleExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.approvalflow.ApprovalFlowScheduleNodeRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目节点与审批人员关系顺序
 *
 * @author song
 * @date 2018/10/13 17:32
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ApprovalFlowScheduleNodeRoleServiceImpl implements ApprovalFlowScheduleNodeRoleService {

    @Resource
    private ApprovalFlowScheduleNodeRoleMapper scheduleNodeRoleMapper;

    @Override
    public List<ApprovalFlowScheduleNodeRole> findLastVersionByNodeNumAndScheduleSort(String nodeNum, Integer scheduleSort) {
        return scheduleNodeRoleMapper.findLastVersionByNodeNumAndScheduleSort(nodeNum, scheduleSort);
    }

    @Override
    public void create(List<? extends ApprovalFlowNode> nodes, List<List<UserRoleSet>> roleList, Integer scheduleSort, Integer scheduleVersion) {
        for (int index = 0; index < nodes.size(); index++) {
            for (UserRoleSet role : roleList.get(index)) {
                ApprovalFlowScheduleNodeRole scheduleNodeRole = new ApprovalFlowScheduleNodeRole();
                scheduleNodeRole.setNodeNum(nodes.get(index).getNum());
                scheduleNodeRole.setRoleId(role.getRoleCode());
                scheduleNodeRole.setScheduleSort(scheduleSort);
                scheduleNodeRole.setScheduleVersion(scheduleVersion);
                scheduleNodeRoleMapper.insertSelective(scheduleNodeRole);
            }
        }
    }

    @Override
    public List<List<ApprovalFlowScheduleNodeRole>> findByNodesAndScheduleSortAndVersion(List<? extends ApprovalFlowNode> nodes, Integer scheduleSort, Integer scheduleVersion) {
        List<List<ApprovalFlowScheduleNodeRole>> scheduleNodeRoleList = new ArrayList<>(nodes.size());
        boolean existConfig = false;
        for (ApprovalFlowNode node : nodes) {
            List<ApprovalFlowScheduleNodeRole> scheduleNodeRoles = findByNodeNumAndScheduleSortAndVersion(node.getNum(), scheduleSort, scheduleVersion);
            if (scheduleNodeRoles == null) {
                scheduleNodeRoles = new ArrayList<>();
            } else if (scheduleNodeRoles.size() > 0) {
                existConfig = true;
            }
            scheduleNodeRoleList.add(scheduleNodeRoles);
        }
        return existConfig ? scheduleNodeRoleList : null;
    }

    private List<ApprovalFlowScheduleNodeRole> findByNodeNumAndScheduleSortAndVersion(String nodeNum, Integer scheduleSort, Integer scheduleVersion) {
        ApprovalFlowScheduleNodeRoleExample example = new ApprovalFlowScheduleNodeRoleExample();
        example.createCriteria().andNodeNumEqualTo(nodeNum).andScheduleSortEqualTo(scheduleSort).andScheduleVersionEqualTo(scheduleVersion);
        return scheduleNodeRoleMapper.selectByExample(example);
    }
}