package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.UserRoleSet;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/24 11:32
 */
@Data
public class ApprovalSequenceVO {
    private List<ApprovalFlowNodeRoleVO> nodeRoles;
    private List<UserRoleSet> roles;
}
