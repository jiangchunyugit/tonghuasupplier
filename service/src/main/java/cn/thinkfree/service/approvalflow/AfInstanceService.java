package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.vo.AfInstanceDetailVO;
import cn.thinkfree.database.vo.AfInstanceListVO;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 10:22
 */
public interface AfInstanceService {

    AfInstanceDetailVO start(String projectNo, String userId, String configNo, Integer scheduleSort);

    void submitStart(String projectNo, String userId, String configNo, Integer scheduleSort, String data, String remark);

    AfInstanceDetailVO detail(String instanceNo, String userId);

    void approval(String instanceNo, String userId, Integer option, String remark);

    AfInstanceListVO list(String userId, String projectNo, String approvalType, Integer scheduleSort);

    /**
     * 获取项目的审批结果
     * @param projectNo 项目编号
     * @return 审批结果，0：未审批或正在审批；1：同意；2：拒绝
     */
    int getProjectCheckResult(String projectNo);

    /**
     * 批量获取项目的审批结果
     * @param projectNos 项目编号
     * @return 审批结果，null：未审批或正在审批；1：同意；2：拒绝
     */
    Map<String, Integer> getProjectCheckResult(List<String> projectNos);

    AfInstanceDetailVO Mstart(String projectNo, String userId, String configNo, Integer scheduleSort);

    void MsubmitStart(String projectNo, String userId, String configNo, Integer scheduleSort, String data, String remark);

    AfInstanceDetailVO Mdetail(String instanceNo, String userId);

    void Mapproval(String instanceNo, String userId, Integer option, String remark);

    AfInstanceListVO Mlist(String userId, String projectNo, String approvalType, Integer scheduleSort);

}
