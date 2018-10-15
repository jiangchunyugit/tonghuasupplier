package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.model.ApprovalFlowConfigLog;
import cn.thinkfree.database.model.UserRoleSet;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 审批流详细信息
 * @author songchuanrang
 */
public class ApprovalFlowConfigLogVO {
    /**
     * 审批流信息
     */
    @ApiModelProperty("审批流对象")
    private ApprovalFlowConfig config;
    /**
     * 审批节点信息
     */
    @ApiModelProperty("审批流节点信息")
    private List<ApprovalFlowNodeVO> nodeVos;
    /**
     * 审批流修改日志表
     */
    @ApiModelProperty("审批流修改日志d")
    private List<ApprovalFlowConfigLog> configLogs;

    @ApiModelProperty("角色信息集合")
    private List<UserRoleSet> roles;

    public ApprovalFlowConfig getConfig() {
        return config;
    }

    public void setConfig(ApprovalFlowConfig config) {
        this.config = config;
    }

    public List<ApprovalFlowNodeVO> getNodeVos() {
        return nodeVos;
    }

    public void setNodeVos(List<ApprovalFlowNodeVO> nodeVos) {
        this.nodeVos = nodeVos;
    }

    public List<ApprovalFlowConfigLog> getConfigLogs() {
        return configLogs;
    }

    public void setConfigLogs(List<ApprovalFlowConfigLog> configLogs) {
        this.configLogs = configLogs;
    }

    public List<UserRoleSet> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleSet> roles) {
        this.roles = roles;
    }
}
