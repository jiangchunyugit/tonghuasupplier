package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import com.github.pagehelper.PageInfo;

public interface ConstructionOrderOperate {

    /**
     * 施工订单列表统计
     * @return
     */
    MyRespBundle<ConstructionOrderManageVo> getOrderNum();

    /**
     *  订单列表
     * @param pageNum
     * @param pageSize
     * @param cityName
     * @param orderType
     * @return
     */
    PageInfo<ConstructionOrderListVo> getOrderList(int pageNum, int pageSize, String cityName, int orderType);

}
