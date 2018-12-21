package cn.thinkfree.core.event;

public interface EventGenerator {
    /**
     * 发布事件
     * @param event
     * @return
     */
    String publish( BaseEvent event);
}
