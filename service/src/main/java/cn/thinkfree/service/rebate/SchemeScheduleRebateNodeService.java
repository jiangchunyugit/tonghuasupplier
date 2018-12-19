package cn.thinkfree.service.rebate;

import cn.thinkfree.database.vo.rebate.SchemeScheduleRebateNodeVO;

/**
 * 方案返款节点服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/19 10:36
 */
public interface SchemeScheduleRebateNodeService {

    /**
     * 列出排期与返款节点对应关系
     * @param schemeNo 方案编号
     * @return 排期与返款节点对应关系
     */
    SchemeScheduleRebateNodeVO list(String schemeNo);

    /**
     * 修改排期与返款节点对应关系
     * @param schemeScheduleRebateNodeVO 排期与返款节点对应关系
     */
    void edit(SchemeScheduleRebateNodeVO schemeScheduleRebateNodeVO);
}
