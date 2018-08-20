package cn.thinkfree.database.event;

import cn.thinkfree.core.model.AbsMyEvent;

public class ProjectUpOnline  extends AbsMyEvent {

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

    public ProjectUpOnline(String projectNo){
        this.source = projectNo;
    }

}

