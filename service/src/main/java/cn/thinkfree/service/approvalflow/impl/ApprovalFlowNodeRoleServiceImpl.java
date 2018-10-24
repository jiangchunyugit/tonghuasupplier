package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowNodeRoleMapper;
import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.model.ApprovalFlowNodeExample;
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
    public List<ApprovalFlowNodeRole> findSendRoleByNodeNum(String nodeNum) {
        return findByNodeNumAndType(nodeNum, 0);
    }

    @Override
    public void deleteByNodeNums(List<String> nodeNums) {
        if (nodeNums != null && nodeNums.size() > 0 ) {
            ApprovalFlowNodeRoleExample example = new ApprovalFlowNodeRoleExample();
            example.createCriteria().andNodeNumIn(nodeNums);
            nodeRoleMapper.deleteByExample(example);
        }
    }

    @Override
    public List<ApprovalFlowNodeRole> findReceiveRoleByNodeNum(String nodeNum) {
        return findByNodeNumAndType(nodeNum, 1);
    }

    private List<ApprovalFlowNodeRole> findByNodeNumAndType(String nodeNum, Integer type) {
        ApprovalFlowNodeRoleExample example = new ApprovalFlowNodeRoleExample();
        example.createCriteria().andNodeNumEqualTo(nodeNum).andTypeEqualTo(type);
        return nodeRoleMapper.selectByExample(example);
    }
}
