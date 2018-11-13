package cn.thinkfree.database.vo;

import lombok.Data;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/11/13 11:28
 */
@Data
public class AfUserVO {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 角色Id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;
}
