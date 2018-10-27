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

    List<UserRoleSet> findByPlanNo(String planNo, List<UserRoleSet> allRoles);

    void create(String planNo, List<UserRoleSet> roles);

    List<AfApprovalRole> findByPlanNo(String planNo);
}
