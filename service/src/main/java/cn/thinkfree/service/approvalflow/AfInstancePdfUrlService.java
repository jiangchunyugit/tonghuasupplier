package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfInstance;

/**
 * 审批流实例pdf
 *
 * @author song
 * @version 1.0
 * @date 2018/11/13 15:56
 */
public interface AfInstancePdfUrlService {

    void create(AfInstance instance);
}
