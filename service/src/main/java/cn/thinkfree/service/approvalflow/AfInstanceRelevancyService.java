package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfInstanceRelevancy;

/**
 * 审批流实例关联服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/6 18:37
 */
public interface AfInstanceRelevancyService {

    /**
     * 创建审批流实例关联
     * @param sourceInstanceNo 数据来源实例编号
     * @param relevancyInstanceNo 关联实例编号
     */
    void create(String sourceInstanceNo, String relevancyInstanceNo);

    /**
     * 通过关联实例编号查询实例关联信息
     * @param relevancyInstanceNo 关联实例编号
     * @return 实例关联信息
     */
    AfInstanceRelevancy findByRelevancyInstanceNo(String relevancyInstanceNo);
}
