package cn.thinkfree.database.event.sync;

import java.util.List;

import cn.thinkfree.core.event.AbsBaseEvent;
import cn.thinkfree.database.vo.remote.SyncOrderVO;

/**
 * 创建订单
 */
    public class CreateOrder extends AbsBaseEvent {

    private String source;

    private List<SyncOrderVO> data;

    public CreateOrder(String source) {
        this.source = source;
    }

    public CreateOrder() {
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

	public List<SyncOrderVO> getData() {
		return data;
	}

	public void setData(List<SyncOrderVO> data) {
		this.data = data;
	}

   


}
