package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfConfigPlan;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/30 17:01
 */
public interface AfConfigPlanService {
    List<AfConfigPlan> findByPlanNo(String planNo);

    AfConfigPlan findByConfigNoAndPlanNo(String configNo, String planNo);

    void create(String configPlanNo, String configNo, String planNo, String describe, String userId);

}
