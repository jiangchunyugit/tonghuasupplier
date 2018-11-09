package cn.thinkfree.service.event;

import cn.thinkfree.core.event.AbsBaseEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EventServiceImpl implements EventService {

    //上下文对象
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 发布事件
     *
     * @param event
     * @return
     */
    @Override
    public String publish(AbsBaseEvent event) {
        //TODO 事件追溯相关操作
        applicationContext.publishEvent(event);
        return "SUCCESS";
    }
}
