package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 审批流详情信息，返回给H5页面进行审批的数据
 * @author xusonghui
 */
@ApiModel("审批流实例详情")
@Data
public class ApprovalFlowInstanceDetailVO {

    /**
     *  项目信息
     */
    @ApiModelProperty("项目信息")
    private ApprovalFlowProjectVO project;
    /**
     * 业主姓名
     */
    @ApiModelProperty("业主姓名")
    private String customerName;
    /**
     * 审批流配置记录编号
     */
    @ApiModelProperty("审批流配置记录编号")
    private String configLogNum;
    /**
     * 当前审批流名称
     */
    @ApiModelProperty("当前审批流名称")
    private String approvalFlowName;
    /**
     * 审批流实例编号
     */
    @ApiModelProperty("审批流实例编号")
    private String instanceNum;
    /**
     * 审批人列表
     */
    @ApiModelProperty("审批人列表")
    private List<ApprovalFlowUserVO> userVOs;
    /**
     * 审批流节点编号
     */
    @ApiModelProperty("审批流实例编号")
    private String nodeNum;
    /**
     * 当前审批流节点注释
     */
    @ApiModelProperty("当前审批流节点注释")
    private String nodeDescribe;
    /**
     * 当前审批节点操作按钮信息
     */
    @ApiModelProperty("审批节点操作按钮信息")
    private List<ApprovalFlowOption> options;
    /**
     * 审批数据
     */
    @ApiModelProperty("审批数据")
    private String data;
    /**
     * 是否可操作表单
     */
    @ApiModelProperty("是否可操作表单")
    private Boolean editable;
    /**
     * 管家名称
     */
    @ApiModelProperty("管家名称")
    private String housekeeper;
    /**
     * 项目经理
     */
    @ApiModelProperty("项目经理")
    private String projectManager;
    /**
     * 工长名称
     */
    @ApiModelProperty("工长名称")
    private String foreman;
}
