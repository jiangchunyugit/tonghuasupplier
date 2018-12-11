package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.vo.ConstructCountVO;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import com.github.pagehelper.PageInfo;

public interface ConstructOrderService {

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

    /**
     * 施工订单统计
     * @param userId 用户编号
     * @param approvalType 审批单类型
     * @return 施工订单统计
     */
    ConstructCountVO count(String userId, String approvalType);

}
