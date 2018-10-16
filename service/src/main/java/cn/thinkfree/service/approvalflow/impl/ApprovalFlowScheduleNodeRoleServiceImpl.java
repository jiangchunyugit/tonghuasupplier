package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowScheduleNodeRoleMapper;
import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRole;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.approvalflow.ApprovalFlowScheduleNodeRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ApprovalFlowScheduleNodeRoleServiceImpl
 * @Description 项目节点与审批人员关系顺序
 * @Author song
 * @Data 2018/10/13 17:32
 * @Version 1.0
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
    public void create(List<? extends ApprovalFlowNode> nodes, List<List<UserRoleSet>> roleList, Integer scheduleSort) {
        for (int index = 0; index < nodes.size(); index++) {
            for (UserRoleSet role : roleList.get(index)){
                ApprovalFlowScheduleNodeRole scheduleNodeRole = new ApprovalFlowScheduleNodeRole();
                scheduleNodeRole.setNodeNum(nodes.get(index).getNum());
                scheduleNodeRole.setRoleId(role.getRoleCode());
                scheduleNodeRole.setScheduleSort(scheduleSort);
                scheduleNodeRole.setVersion(1);
                scheduleNodeRoleMapper.insertSelective(scheduleNodeRole);
            }
        }
    }

    @Override
    public void update(List<? extends ApprovalFlowNode> nodes, List<List<UserRoleSet>> roleList, Integer scheduleSort) {
        boolean existConfig = false;
        for (int index = 0; index < nodes.size(); index++) {
            List<ApprovalFlowScheduleNodeRole> scheduleNodeRoles = findLastVersionByNodeNumAndScheduleSort(nodes.get(index).getNum(), scheduleSort);
            if (scheduleNodeRoles != null && scheduleNodeRoles.size() > 0) {
                existConfig = true;
                for (ApprovalFlowScheduleNodeRole scheduleNodeRole : scheduleNodeRoles) {
                    scheduleNodeRole.setVersion(scheduleNodeRole.getVersion() + 1);
                    scheduleNodeRoleMapper.updateByPrimaryKeySelective(scheduleNodeRole);
                }
            }
        }
        if (!existConfig) {
            create(nodes, roleList, scheduleSort);
        }
    }

}
