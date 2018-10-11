package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * 审批流节点审批角色顺序
 */
public class NodeRoleSequenceVo {
    // 节点编号
    private String nodeNum;
    // 序号
    private int sort;
    // 角色
    private List<UserRoleSet> roles;

    public String getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(String nodeNum) {
        this.nodeNum = nodeNum;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<UserRoleSet> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleSet> roles) {
        this.roles = roles;
    }
}
