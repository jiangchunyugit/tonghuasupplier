package cn.thinkfree.service.event;

import cn.thinkfree.core.event.AbsBaseEvent;
import cn.thinkfree.core.event.BaseEvent;

/**
 * 事件服务
 */
public interface  EventService {

    /**
     * 发布事件
     * @param event
     * @return
     */
    String publish( BaseEvent event);
}
