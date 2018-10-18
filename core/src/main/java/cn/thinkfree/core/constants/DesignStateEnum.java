package cn.thinkfree.core.constants;

/**
 * @author xusonghui
 * 设计订单枚举类
 */

public enum DesignStateEnum {
    /**
     * 平台待派单
     */
    STATE_1(1, "平台待派单"),
    /**
     * 设计公司待派单
     */
    STATE_10(10, "设计公司待派单"),
    /**
     * 平台不派单
     */
    STATE_20(20, "平台不派单"),
    /**
     * 设计公司拒绝接单
     */
    STATE_30(30, "设计公司拒绝接单"),
    /**
     * 设计师公司已派单
     */
    STATE_40(40, "设计师公司已派单"),
    /**
     * 设计师拒绝接单
     */
    STATE_50(50, "设计师拒绝接单"),
    /**
     * 设计师接单
     */
    STATE_60(60, "设计师接单"),
    /**
     * 量房预约
     */
    STATE_70(70, "量房预约"),
    /**
     * 待支付量房费
     */
    STATE_80(80, "待支付量房费"),
    /**
     * 待提交量房图
     */
    STATE_90(90, "待提交量房图"),
    /**
     * 待签订设计合同
     */
    STATE_100(100, "待签订设计合同"),
    /**
     * 已支付首款
     */
    STATE_110(110, "已支付首款"),
    /**
     * 设计师提交3D效果图
     */
    STATE_120(120, "设计师提交3D效果图"),
    /**
     * 已支付中期款
     */
    STATE_130(130, "已支付中期款"),
    /**
     * 待提交施工资料
     */
    STATE_140(140, "待提交施工资料"),
    /**
     * 已支付尾款
     */
    STATE_150(150, "已支付尾款"),
    /**
     * 订单待转换
     */
    STATE_160(160, "订单待转换");
    private int state;
    private String stateName;

    DesignStateEnum(int state, String stateName) {
        this.state = state;
        this.stateName = stateName;
    }

    public int getState() {
        return state;
    }

    public String getStateName() {
        return stateName;
    }
}
