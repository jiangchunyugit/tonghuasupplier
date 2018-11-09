package cn.thinkfree.database.event;

import cn.thinkfree.core.event.AbsBaseEvent;

/**
 * 发送验证码
 */
public class SendValidateCode extends AbsBaseEvent {

    /**
     * 事件源
     */
    private String source;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;


    public SendValidateCode() {
    }

    public SendValidateCode(String source, String phone, String code) {
        this.source = source;
        this.phone = phone;
        this.code = code;
    }

    /**
     * 获取事件源
     *
     * @return
     */
    @Override
    public String getSource() {
        return source;
    }

    public String getPhone() {
        return phone;
    }

    public String getCode() {
        return code;
    }
}
