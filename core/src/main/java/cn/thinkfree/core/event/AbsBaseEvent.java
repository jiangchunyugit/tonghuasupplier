package cn.thinkfree.core.event;

import cn.thinkfree.core.model.BaseModel;

public abstract class AbsBaseEvent extends BaseModel {


    /**
     * 获取事件源
     * @return
     */
    public abstract String getSource();

    /**
     * 获取事件类型
     * @return
     */
    public EventType getEventType(){
        return EventType.SYSTEM;
    }
}
