package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.ConstructionOrderCommonVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import com.github.pagehelper.PageInfo;
import cn.thinkfree.service.construction.vo.SiteDetailsVo;

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
     * @return
     */
    MyRespBundle<ConstructionOrderCommonVo> getOrderList(int pageNum, int pageSize, String cityName);

    /**
     * 施工工地管理-列表
     * 运营后台
     * @return
     */
    MyRespBundle<ConstructionOrderManageVo> getConstructionSiteList(int pageNum, int pageSize, String cityName);
}
