package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfCompanyPlan;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/29 10:16
 */
public interface AfCompanyPlanService {

    List<AfCompanyPlan> findByCompanyNoAndConfigNo(String companyNo, String configNo);
}
