package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfInstance;
import cn.thinkfree.database.model.AfInstancePdfUrl;

import java.util.List;

/**
 * 审批流实例pdf
 *
 * @author song
 * @version 1.0
 * @date 2018/11/13 15:56
 */
public interface AfInstancePdfUrlService {

    /**
     * 创建审批流实例pdf
     * @param instance
     */
    void create(AfInstance instance);

    /**
     * 根据项目编号查询审批流实例pdf
     * @param projectNo
     * @return
     */
    List<AfInstancePdfUrl> findByProjectNo(String projectNo);
}
