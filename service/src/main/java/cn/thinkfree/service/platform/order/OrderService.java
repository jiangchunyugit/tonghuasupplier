package cn.thinkfree.service.platform.order;

import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 订单状态服务
 */
public interface OrderService {

    List<Map<String, Object>> allState();

    /**
     * 发送预交底得信息
     *
     * @param projectNo     项目编号
     * @param designerId    设计师ID
     * @param subscribeTime 预约时间，如：2018年12月11日
     * @param remark        描述
     */
    void sendPredatingMsg(String projectNo, String designerId, String subscribeTime, String remark);
}
