package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfInstanceRelevance;

/**
 * 审批流实例关联服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/6 18:37
 */
public interface AfInstanceRelevanceService {

    /**
     * 创建审批流实例关联
     * @param sourceInstanceNo 数据来源实例编号
     * @param relevanceInstanceNo 关联实例编号
     * @param data 关联实例数据
     */
    void create(String sourceInstanceNo, String relevanceInstanceNo, String data);

    /**
     * 通过关联实例编号查询实例关联信息
     * @param relevanceInstanceNo 关联实例编号
     * @return 实例关联信息
     */
    AfInstanceRelevance findByRelevanceInstanceNo(String relevanceInstanceNo);
}
