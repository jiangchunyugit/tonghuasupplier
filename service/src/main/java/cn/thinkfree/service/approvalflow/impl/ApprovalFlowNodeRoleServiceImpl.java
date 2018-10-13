package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowNodeRoleMapper;
import cn.thinkfree.database.model.ApprovalFlowNodeRole;
import cn.thinkfree.service.approvalflow.ApprovalFlowNodeRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点与角色的关联关系服务层
 */
@Service
@Transactional
public class ApprovalFlowNodeRoleServiceImpl implements ApprovalFlowNodeRoleService {

    @Resource
    private ApprovalFlowNodeRoleMapper nodeRoleMapper;

    /**
     * 创建审批流节点与角色的关联关系
     * @param nodeNum 审批流节点编号
     * @param nodeRoles 节点与角色的关联关系
     */
    @Override
    public void create(String nodeNum, List<ApprovalFlowNodeRole> nodeRoles) {
        if (null != nodeRoles){
            for (ApprovalFlowNodeRole nodeRole : nodeRoles) {
                nodeRole.setId(0);
                nodeRole.setNodeNum(nodeNum);
                nodeRoleMapper.insert(nodeRole);
            }
        }
    }

    @Override
    public List<ApprovalFlowNodeRole> findLastVersionByNodeNumAndProjectBigSchedulingId(String nodeNum, Long projectBigSchedulingId) {
        return nodeRoleMapper.findLastVersionByNodeNumAndProjectBigSchedulingId(nodeNum, projectBigSchedulingId);
    }
}
