package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.AfPlanVO;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 15:53
 */
public interface AfPlanService {

    List<AfPlanVO> findByConfigLogNo(String configLogNo, List<UserRoleSet> roles);

    void create(String configLogNo, List<AfPlanVO> planVOs);
}
