package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.vo.AfInstanceDetailVO;
import cn.thinkfree.database.vo.AfInstanceListVO;

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

    AfInstanceDetailVO Mstart(String projectNo, String userId, String configNo, Integer scheduleSort);

    void MsubmitStart(String projectNo, String userId, String configNo, Integer scheduleSort, String data, String remark);

    AfInstanceDetailVO Mdetail(String instanceNo, String userId);

    void Mapproval(String instanceNo, String userId, Integer option, String remark);

    AfInstanceListVO Mlist(String userId, String projectNo, String approvalType, Integer scheduleSort);
}
