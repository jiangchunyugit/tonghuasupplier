package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import com.github.pagehelper.PageInfo;

public interface DecorationOrderOperate {

    /**
     * 施工订单列表统计
     * @return
     */
    MyRespBundle<ConstructionOrderManageVo> getDecorationtOrderNum();

    /**
     *  订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<ConstructionOrderListVo> getDecorationOrderList(String companyNo, int pageNum, int pageSize);
}
