package cn.thinkfree.service.rebate;

/**
 * 返款节点日志
 *
 * @author song
 * @version 1.0
 * @date 2018/12/19 15:46
 */
public interface FundsSettleAccountsNodeLogService {

    /**
     * 创建返款节点日志
     * @param schemeNo 方案编号
     * @param companyId 公司编号
     * @param projectNo 项目编号
     * @param orderNo 订单编号
     * @param scheduleSort 排期编号
     */
    void create(String schemeNo, String companyId, String projectNo, String orderNo, Integer scheduleSort);
}
