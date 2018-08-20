package cn.thinkfree.database.event;

import cn.thinkfree.core.model.AbsMyEvent;

public class ProjectStopEvent extends AbsMyEvent {

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
