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
     * 手机号 或邮箱
     */
    private String target;

    /**
     * 验证码
     */
    private String code;

    /**
     * 是否手机端 如果不是则是邮箱
     */
    private Boolean isPhone;


    public SendValidateCode() {
    }

    public SendValidateCode(String source, String phone, String code) {
        this.source = source;
        this.target = phone;
        this.code = code;
        this.isPhone = false;
    }
    public SendValidateCode(String source, String phone, String code,Boolean isPhone) {
        this.source = source;
        this.target = phone;
        this.code = code;
        this.isPhone = isPhone;
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

    public String getTarget() {
        return target;
    }

    public String getCode() {
        return code;
    }

    public Boolean getIsPhone(){
        return isPhone;
    }
}
