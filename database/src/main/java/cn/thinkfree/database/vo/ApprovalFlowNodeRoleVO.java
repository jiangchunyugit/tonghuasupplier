package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.UserRoleSet;
import lombok.Data;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/24 11:33
 */
@Data
public class ApprovalFlowNodeRoleVO {
    private String nodeNum;
    private UserRoleSet role;
}
