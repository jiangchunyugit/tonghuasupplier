package cn.thinkfree.core.event;

import com.google.common.eventbus.EventBus;

/**
 * 事件总线 封装
 */
public class MyEventBus {
    private static EventBus eventBus;

    private static class SingletonHolder {
        private static final MyEventBus INSTANCE = new MyEventBus();
    }


    private MyEventBus(){
        eventBus = new EventBus("MyEventBus");
    }

    public static MyEventBus getInstance(){
        return SingletonHolder.INSTANCE;
    }


    public   void  publicEvent(Object obj){
        eventBus.post(obj);
    }

    public   void register(Object obj){
        eventBus.register(obj);
    }
}
