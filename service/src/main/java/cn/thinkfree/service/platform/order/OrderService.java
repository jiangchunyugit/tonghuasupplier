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

    /**
     * 发送预交底得信息
     *
     * @param projectNo     项目编号
     * @param designerId    设计师ID
     * @param subscribeTime 预约时间，如：2018年12月11日
     * @param remark        描述
     */
    void sendPredatingMsg(String projectNo, String designerId, String subscribeTime, String remark);

    /**
     * 发送设计师接单通知给业主
     *
     * @param ownerId   业主ID
     * @param projectNo 项目编号
     */
    void sendDesignerReceipt(String ownerId, String projectNo);

    /**
     * 发送给施工人员
     *
     * @param userId
     * @param projectNo
     */
    void sendEmployeeReceipt(String userId, String projectNo);

    /**
     * 发送设计公司派单消息给设计师
     *
     * @param designerId 设计师ID
     * @param projectNo  项目编号
     */
    void sendCompanyDispatch(String designerId, String projectNo);

    /**
     * 运营平台派单给小B公司的通知
     *
     * @param companyId     公司ID
     * @param projectNo     项目编号
     * @param orderTypeName 订单类型，施工订单，设计订单
     */
    void sendPlatformDispatch(String companyId, String projectNo, String orderTypeName);

    /**
     * 设计师拒绝接单
     *
     * @param companyId    公司ID
     * @param projectNo    项目编号
     * @param designerName 设计师名称
     */
    void sendDesignerNoReceipt(String companyId, String projectNo, String designerName);

    /**
     * 发送装饰公司派单成功的消息给业主
     *
     * @param ownerId   业主ID
     * @param projectNo 项目编号
     */
    void sendOwnerConsDispatch(String ownerId, String projectNo);
}
