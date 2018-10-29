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

    AfInstanceDetailVO start(String projectNo, String userId, String configNo);

    void submitStart(String projectNo, String userId, String planNo, Integer scheduleSort, String data, String remark);

    AfInstanceDetailVO detail(String instanceNo, String userId);

    void approval(String instanceNo, String userId, Integer option, String remark);

    AfInstanceListVO list(String userId, String projectNo, Integer scheduleSort);
}
