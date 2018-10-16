package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.model.ApprovalFlowConfigLog;
import cn.thinkfree.database.model.ApprovalFlowConfigSuper;
import cn.thinkfree.database.model.UserRoleSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 审批流详细信息
 * @author songchuanrang
 */
@ApiModel("审批流详细信息")
@Data
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

    @ApiModelProperty("当前审批流依托关系")
    private List<ApprovalFlowConfigSuper> configSupers;

    @ApiModelProperty("审批流信息集合")
    private List<ApprovalFlowConfig> configs;

}
