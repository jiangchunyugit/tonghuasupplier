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
    public List<ApprovalFlowScheduleNodeRole> findLastVersionByNodeNumAndCompanyNoAndScheduleSort(String nodeNum, String companyNo, Integer scheduleSort) {
        return scheduleNodeRoleMapper.findLastVersionByNodeNumAndCompanyNoAndScheduleSort(nodeNum, companyNo, scheduleSort);
    }

    @Override
    public void create(List<? extends ApprovalFlowNode> nodes, List<UserRoleSet> roles, String companyNo, Integer scheduleSort, Integer scheduleVersion) {
        for (int index = 0; index < nodes.size(); index++) {
            ApprovalFlowScheduleNodeRole scheduleNodeRole = new ApprovalFlowScheduleNodeRole();
            scheduleNodeRole.setNodeNum(nodes.get(index).getNum());
            scheduleNodeRole.setRoleId(roles.get(index).getRoleCode());
            scheduleNodeRole.setCompanyNo(companyNo);
            scheduleNodeRole.setScheduleSort(scheduleSort);
            scheduleNodeRole.setScheduleVersion(scheduleVersion);
            scheduleNodeRoleMapper.insertSelective(scheduleNodeRole);
        }
    }

    @Override
    public List<ApprovalFlowScheduleNodeRole> findByNodesAndCompanyNoAndScheduleSortAndVersion(List<? extends ApprovalFlowNode> nodes, String companyNo, Integer scheduleSort, Integer scheduleVersion) {
        if (nodes != null && nodes.size() > 0) {
            List<ApprovalFlowScheduleNodeRole> scheduleNodeRoleList = new ArrayList<>(nodes.size());
            List<String> nodeNums = new ArrayList<>(nodes.size());
            for (ApprovalFlowNode node : nodes) {
                nodeNums.add(node.getNum());
            }
            if (nodeNums.size() > 0) {
                ApprovalFlowScheduleNodeRoleExample example = new ApprovalFlowScheduleNodeRoleExample();
                example.createCriteria().andCompanyNoEqualTo(companyNo).andNodeNumIn(nodeNums).andScheduleSortEqualTo(scheduleSort).andScheduleVersionEqualTo(scheduleVersion);
                return scheduleNodeRoleMapper.selectByExample(example);
            }
        }
        return null;
    }

    @Override
    public List<ApprovalFlowScheduleNodeRole> findByNodeNumAndCompanyNoAndScheduleSortAndVersion(String nodeNum, String companyNo, Integer scheduleSort, Integer scheduleVersion) {
        ApprovalFlowScheduleNodeRoleExample example = new ApprovalFlowScheduleNodeRoleExample();
        example.createCriteria().andNodeNumEqualTo(nodeNum).andScheduleSortEqualTo(scheduleSort).andScheduleVersionEqualTo(scheduleVersion);
        return scheduleNodeRoleMapper.selectByExample(example);
    }
}