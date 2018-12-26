package cn.thinkfree.core.constants;

/**
 * @Auther: jiang
 * @Date: 2018/12/23 14:39
 * @Description:客诉状态
 */
public enum ComplaintStateEnum {
    /**
     * 客诉情况：未投诉
     */
    STATE_1(1, "未投诉"),
    /**
     * 客诉情况：投诉处理中
     */
    STATE_2(2, "投诉处理中"),
    /**
     * 客诉情况：订单关闭
     */
    STATE_3(3, "订单关闭"),
    /**
     * 客诉情况：取消投诉
     */
    STATE_4(4, "取消投诉"),
    /**
     * 客诉情况：未投诉，直接取消订单
     */
    STATE_5(5, "未投诉，直接取消订单");

    private int state;
    private String code;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    ComplaintStateEnum(int state, String code) {
        this.state = state;
        this.code = code;
    }
}
