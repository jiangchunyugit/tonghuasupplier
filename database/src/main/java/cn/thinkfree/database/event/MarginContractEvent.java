package cn.thinkfree.database.event;

import cn.thinkfree.core.model.AbsMyEvent;

/**
 * @author jiangchunyu(后台)
 * @date 20181109
 * @Description 合同相关数据事件
 */
public class MarginContractEvent extends AbsMyEvent {

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

    public MarginContractEvent(String projectNo){
        this.source = projectNo;
    }

}
