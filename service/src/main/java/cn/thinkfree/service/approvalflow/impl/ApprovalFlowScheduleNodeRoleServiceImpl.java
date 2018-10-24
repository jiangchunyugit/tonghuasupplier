package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.database.mapper.ApprovalFlowScheduleNodeRoleMapper;
import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRole;
import cn.thinkfree.database.model.ApprovalFlowScheduleNodeRoleExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.ApprovalFlowNodeRoleVO;
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

    private static final MyLogger LOGGER = new MyLogger(ApprovalFlowScheduleNodeRoleService.class);

    @Resource
    private ApprovalFlowScheduleNodeRoleMapper scheduleNodeRoleMapper;

    @Override
    public List<ApprovalFlowScheduleNodeRole> findLastVersionByNodeNumAndCompanyNoAndScheduleSort(String nodeNum, String companyNo, Integer scheduleSort) {
        return scheduleNodeRoleMapper.findLastVersionByNodeNumAndCompanyNoAndScheduleSort(nodeNum, companyNo, scheduleSort);
    }

    @Override
    public void create(List<ApprovalFlowNode> nodes, List<ApprovalFlowNodeRoleVO> nodeRoles, String companyNo, Integer scheduleSort, Integer scheduleVersion) {
        if (nodes.size() != nodeRoles.size()) {
            LOGGER.error("审批顺序集合大小不匹配");
            throw new RuntimeException();
        }
        for (int index = 0; index < nodes.size(); index++) {
            ApprovalFlowNode node = nodes.get(index);
            ApprovalFlowNodeRoleVO nodeRoleVO = nodeRoles.get(index);
            if (!node.getNum().equals(nodeRoleVO.getNodeNum())) {
                LOGGER.error("审批顺序节点编号顺序不正确，nodeNum:({}:{})", node.getNum(), nodeRoleVO.getNodeNum());
                throw new RuntimeException();
            }
            ApprovalFlowScheduleNodeRole scheduleNodeRole = new ApprovalFlowScheduleNodeRole();
            scheduleNodeRole.setNodeNum(node.getNum());
            scheduleNodeRole.setRoleId(nodeRoleVO.getRole().getRoleCode());
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