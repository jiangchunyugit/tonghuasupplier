package cn.thinkfree.service.construction;

import cn.thinkfree.database.model.ConstructionOrder;

/**
 * 施工订单服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/13 17:00
 */
public interface ConstructOrderService {

    /**
     * 根据项目编号查询施工订单
     * @param projectNo
     * @return
     */
    ConstructionOrder findByProjectNo(String projectNo);
}
