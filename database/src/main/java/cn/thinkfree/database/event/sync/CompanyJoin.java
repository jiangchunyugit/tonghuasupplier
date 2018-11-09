package cn.thinkfree.database.event.sync;

import cn.thinkfree.core.event.AbsBaseEvent;

/**
 * 公司资质完成后同步信息
 */
public class CompanyJoin extends AbsBaseEvent {

    private String source;
    /**
     * 获取事件源
     *
     * @return
     */
    @Override
    public String getSource() {
        return source;
    }

    public CompanyJoin() {
    }

    public CompanyJoin(String source) {
        this.source = source;
    }
}
