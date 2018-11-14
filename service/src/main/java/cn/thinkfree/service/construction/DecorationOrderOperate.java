package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.ConstructionOrderCommonVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;

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
    MyRespBundle<ConstructionOrderCommonVo> getDecorationOrderList(String companyNo,int pageNum, int pageSize);
}
