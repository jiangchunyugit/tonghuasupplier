package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfApprovalRole;
import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
public interface AfApprovalRoleService {

    List<AfApprovalRole> findByConfigLogNo(String configNo);

    List<UserRoleSet> findByConfigLogNo(String configLogNo, List<UserRoleSet> allRoles);

    void create(String configLogNo, List<UserRoleSet> roles);
}
