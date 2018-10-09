package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.database.mapper.ApprovalFlowNodeRoleMapper;
import cn.thinkfree.database.model.ApprovalFlowNodeRole;
import cn.thinkfree.service.approvalFlow.ApprovalFlowNodeRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批流节点与角色的关联关系服务层
 */
@Service
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
                nodeRole.setExternalUniqueCode(nodeNum);
                nodeRoleMapper.insert(nodeRole);
            }
        }
    }
}
