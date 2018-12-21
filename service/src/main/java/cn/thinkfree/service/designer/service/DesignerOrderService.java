package cn.thinkfree.service.designer.service;

import cn.thinkfree.database.model.DesignerOrder;

/**
 * 设计订单服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 16:56
 */
public interface DesignerOrderService {

    /**
     * 根据项目编号查询设计订单
     * @param projectNo
     * @return
     */
    DesignerOrder findByProjectNo(String projectNo);
}
