package cn.thinkfree.database.vo;

import lombok.Data;

/**
 * 变更单变更项（发送至订单项目）
 *
 * @author song
 * @version 1.0
 * @date 2018/12/6 14:08
 */
@Data
public class AfChangeOrderDTO {
    /**
     * 项目编号
     */
    private String projectNo;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 费用名称
     */
    private String feeName;
    /**
     * 费用金额
     */
    private String feeAmount;
    /**
     * 备注
     */
    private String remark;
}
