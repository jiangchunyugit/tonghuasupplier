package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.ConstructionOrder;
import com.github.pagehelper.PageInfo;


public interface ConstructionOrderOperate {

    /**
     * 施工订单管理-列表
     * 运营后台
     * @return
     */
    MyRespBundle<PageInfo<ConstructionOrder>> getConstructionOrderList(int pageNum,int pageSize);
}
