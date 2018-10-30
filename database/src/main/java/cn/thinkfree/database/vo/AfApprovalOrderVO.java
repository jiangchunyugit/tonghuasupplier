package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.AfApprovalOrder;
import cn.thinkfree.database.model.UserRoleSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 15:49
 */
@Data
@ApiModel("审批顺序方案")
public class AfApprovalOrderVO {
    @ApiModelProperty("审批顺序方案")
    private AfApprovalOrder approvalOrder;
    @ApiModelProperty("审批角色顺序")
    List<UserRoleSet> roles;
}
