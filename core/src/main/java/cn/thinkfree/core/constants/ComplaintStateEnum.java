package cn.thinkfree.core.constants;

/**
 * @Auther: jiang
 * @Date: 2018/12/23 14:39
 * @Description:客诉状态
 */
public enum ComplaintStateEnum {
    STATE_1(1, "未投诉"),
    STATE_2(2, "处理中"),
    STATE_3(3, "关闭"),
    STATE_4(4, "已取消");

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
