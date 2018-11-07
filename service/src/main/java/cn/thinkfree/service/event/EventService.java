package cn.thinkfree.service.event;

import cn.thinkfree.core.event.AbsBaseEvent;

/**
 * 事件服务
 */
public interface  EventService {

    /**
     * 发布事件
     * @param event
     * @return
     */
    String publish( AbsBaseEvent event);
}
