package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.ConstructionOrder;
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
     * @param pageNum 页码
     * @param pageSize 每页个数
     * @return 施工订单统计
     */
    ConstructCountVO count(String userId, String approvalType, Integer pageNum, Integer pageSize);

    /**
     * 根据项目编号查询施工订单
     * @param projectNo
     * @return
     */
    ConstructionOrder findByProjectNo(String projectNo);

}
