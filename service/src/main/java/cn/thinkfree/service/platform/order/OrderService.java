package cn.thinkfree.service.platform.order;

import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 订单状态服务
 */
public interface OrderService {
    /**
     * 获取所有的订单状态
     *
     * @return
     */
    List<Map<String, Object>> allState();
}
