package cn.thinkfree.database.event.sync;

import cn.thinkfree.core.event.AbsBaseEvent;

/**
 * 合同完成后 同步用
 */
public class FinishContract extends AbsBaseEvent {

    private String source;

    /**
     * 获取事件源
     *
     * @return
     */
    @Override
    public String getSource() {
        return null;
    }
}
