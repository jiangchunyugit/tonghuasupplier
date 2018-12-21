package cn.thinkfree.database.event;

import cn.thinkfree.core.event.AbsBaseEvent;

public class ProjectStopEvent extends AbsBaseEvent {

    private String source;
    /**
     * 获取 事件源
     *
     * @return
     */
    @Override
    public String getSource() {
        return source;
    }

    public ProjectStopEvent(String projectNo){
        this.source = projectNo;
    }

}
