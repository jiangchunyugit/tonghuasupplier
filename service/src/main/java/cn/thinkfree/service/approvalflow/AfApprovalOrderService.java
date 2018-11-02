package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfApprovalOrder;
import cn.thinkfree.database.model.UserRoleSet;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 15:53
 */
public interface AfApprovalOrderService {

    List<List<UserRoleSet>> findByConfigSchemeNo(String configLogNo, List<UserRoleSet> roles);

    void create(String configSchemeNo, String configNo, List<List<UserRoleSet>> approvalOrders);

    AfApprovalOrder findByConfigSchemeNoAndRoleId(String configSchemeNo, String roleId);

    AfApprovalOrder findByProjectNoAndConfigNoAndUserId(String projectNo, String configNo, String userId);

    AfApprovalOrder findByConfigNoAndSchemeNoAndRoleId(String configNo, String schemeNo, String roleId);
}
