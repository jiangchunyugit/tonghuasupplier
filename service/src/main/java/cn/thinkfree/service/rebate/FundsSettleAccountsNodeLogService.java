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
     * @param projectNo 项目编号
     * @param scheduleSort 排期编号
     */
    void create(String projectNo, Integer scheduleSort);
}
