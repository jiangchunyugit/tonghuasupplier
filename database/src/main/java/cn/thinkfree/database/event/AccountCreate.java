package cn.thinkfree.database.event;

import cn.thinkfree.core.model.AbsMyEvent;

public class AccountCreate extends AbsMyEvent {

    private String source;

    private Object ext;

    /**
     * 获取 事件源
     *
     * @return
     */
    @Override
    public String getSource() {
        return source;
    }
}
