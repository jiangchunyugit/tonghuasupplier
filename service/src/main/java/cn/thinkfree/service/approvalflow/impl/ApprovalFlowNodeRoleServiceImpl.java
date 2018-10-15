package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowNodeRoleMapper;
import cn.thinkfree.database.model.ApprovalFlowNodeRole;
import cn.thinkfree.database.model.ApprovalFlowNodeRoleExample;
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
                nodeRole.setId(null);
                nodeRole.setNodeNum(nodeNum);
                nodeRoleMapper.insertSelective(nodeRole);
            }
        }
    }

    @Override
    public List<ApprovalFlowNodeRole> findByNodeNum(String nodeNum) {
        ApprovalFlowNodeRoleExample example = new ApprovalFlowNodeRoleExample();
        example.createCriteria().andNodeNumEqualTo(nodeNum);
        return nodeRoleMapper.selectByExample(example);
    }

    @Override
    public void deleteByNodeNums(List<String> nodeNums) {
        ApprovalFlowNodeRoleExample example = new ApprovalFlowNodeRoleExample();
        example.createCriteria().andNodeNumIn(nodeNums);
        nodeRoleMapper.deleteByExample(example);
    }
}
