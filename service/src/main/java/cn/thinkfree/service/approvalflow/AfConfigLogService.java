package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfConfig;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 17:27
 */
public interface AfConfigLogService {

    void create(AfConfig config, String configLogNo);
}
