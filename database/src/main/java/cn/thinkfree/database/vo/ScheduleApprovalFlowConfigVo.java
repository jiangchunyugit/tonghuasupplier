package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowFormElementType;
import cn.thinkfree.database.model.UserRoleSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("节点审批信息")
@Data
public class ScheduleApprovalFlowConfigVo {

    @ApiModelProperty("审批流编号")
    private String approvalFlowNum;
    @ApiModelProperty("审批表单")
    private ApprovalFlowFormVo formVo;
    @ApiModelProperty("审批角色顺序")
    private List<List<UserRoleSet>> nodeRoleSequence;
    @ApiModelProperty("所有角色信息")
    private List<UserRoleSet> roles;
    @ApiModelProperty("所有审批表单元素类型")
    private List<ApprovalFlowFormElementType> formElementTypes;
}
