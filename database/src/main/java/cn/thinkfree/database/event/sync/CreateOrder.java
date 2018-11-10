package cn.thinkfree.database.event.sync;

import cn.thinkfree.core.event.AbsBaseEvent;
import cn.thinkfree.database.vo.remote.SyncOrderVO;

/**
 * 创建订单
 */
public class CreateOrder extends AbsBaseEvent {

    private String source;

    private SyncOrderVO data;

    public CreateOrder(String source) {
        this.source = source;
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

    public void setSource(String source) {
        this.source = source;
    }

    public SyncOrderVO getData() {
        return data;
    }

    public void setData(SyncOrderVO data) {
        this.data = data;
    }



}
