package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfSubRole;
import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:52
 */
public interface AfSubRoleService {

    List<AfSubRole> findByConfigPlanNo(String configPlanNo);

    List<UserRoleSet> findByConfigPlanNo(String configPlanNo, List<UserRoleSet> allRoles);

    void create(String configPlanNo, List<UserRoleSet> roles);

}
