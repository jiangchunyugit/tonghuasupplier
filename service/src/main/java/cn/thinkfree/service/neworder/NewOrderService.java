package cn.thinkfree.service.neworder;

import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.Project; /**
 * 订单相关
 * @author gejiaming
 */
public interface NewOrderService {
    /**
     * 获取订单信息
     * @param projectNo
     * @return
     */
    ConstructionOrder getConstructionOrder(String projectNo);
}
