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
public class ApprovalFlowDetailVo {
    /**
     * 审批流信息
     */
    @ApiModelProperty("审批流对象")
    private ApprovalFlowConfig config;
    /**
     * 审批节点信息
     */
    @ApiModelProperty("审批流节点信息")
    private List<ApprovalFlowNodeVo> nodeVos;
    /**
     * 审批流修改日志表
     */
    @ApiModelProperty("审批流修改日志d")
    private List<ApprovalFlowConfigLog> configLogs;

    @ApiModelProperty("角色信息集合")
    private List<UserRoleSet> userRoleSets;

    public ApprovalFlowConfig getConfig() {
        return config;
    }

    public void setConfig(ApprovalFlowConfig config) {
        this.config = config;
    }

    public List<ApprovalFlowNodeVo> getNodeVos() {
        return nodeVos;
    }

    public void setNodeVos(List<ApprovalFlowNodeVo> nodeVos) {
        this.nodeVos = nodeVos;
    }

    public List<ApprovalFlowConfigLog> getConfigLogs() {
        return configLogs;
    }

    public void setConfigLogs(List<ApprovalFlowConfigLog> configLogs) {
        this.configLogs = configLogs;
    }

    public List<UserRoleSet> getUserRoleSets() {
        return userRoleSets;
    }

    public void setUserRoleSets(List<UserRoleSet> userRoleSets) {
        this.userRoleSets = userRoleSets;
    }
}
