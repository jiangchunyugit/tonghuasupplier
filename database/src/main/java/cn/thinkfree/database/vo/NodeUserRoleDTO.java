package cn.thinkfree.database.vo;

import lombok.Data;

import java.util.List;

/**
 * 审批流节点、用户、角色关系
 *
 * @author song
 * @version 1.0
 * @date 2018/10/20 14:59
 */
@Data
public class NodeUserRoleDTO {
    private String nodeNum;
    private List<UserRoleDTO> userRoles;
}
