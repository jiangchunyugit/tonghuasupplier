package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfApprovalLog;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 10:40
 */
public interface AfApprovalLogService {

    AfApprovalLog findByNo(String approvalLogNo);

    List<AfApprovalLog> findByInstanceNo(String instanceNo);

    AfApprovalLog findByInstanceNoAndSort(String instanceNo, Integer sort);

    void updateByPrimaryKey(AfApprovalLog approvalLog);

    List<AfApprovalLog> findByConfigNoAndProjectNoAndUserId(String configNo, String projectNo, String userId);

    List<AfApprovalLog> findByConfigNoAndProjectNoAndScheduleSortAndUserId(String configNo, String projectNo, Integer scheduleSort, String userId);

    void create(List<AfApprovalLog> afApprovalLogs);
}
