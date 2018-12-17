package cn.thinkfree.service.construction;

import cn.thinkfree.database.model.ConstructionOrderPay;

/**
 * 施工订单支付服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/13 17:21
 */
public interface ConstructOrderPayService {

    /**
     * 根据订单编号查询施工订单支付信息
     * @param orderNo 施工订单编号
     * @return 施工订单支付信息
     */
    ConstructionOrderPay findByOrderNo(String orderNo);

    /**
     * 更新施工订单支付信息
     * @param constructionOrderPay 施工订单支付信息
     * @param orderNo 订单编号
     */
    void updateByOrderNo(ConstructionOrderPay constructionOrderPay, String orderNo);

    /**
     * 插入施工订单支付信息
     * @param constructionOrderPay 施工订单支付信息
     */
    void insert(ConstructionOrderPay constructionOrderPay);
}
