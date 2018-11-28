package cn.thinkfree.service.platform.designer;

/**
 * @author xusonghui
 * 创建支付订单
 */
public interface CreatePayOrderService {
    /**
     * 创建量房订单
     * @param projectNo
     */
    void createVolumeRoomPay(String projectNo, String appointmentAmount);
}
