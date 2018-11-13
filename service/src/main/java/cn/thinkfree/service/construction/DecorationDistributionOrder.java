package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.construction.vo.DecorationOrderCommonVo;
import cn.thinkfree.service.construction.vo.appointWorkerListVo;

import java.util.List;
import java.util.Map;

public interface DecorationDistributionOrder {

    /**
     * 订单列表
     *
     * @return
     */
    MyRespBundle<DecorationOrderCommonVo> getOrderList(int pageNum, int pageSize, String projectNo, String appointmentTime,
                                                       String addressDetail, String owner, String phone, String orderStage);

    /**
     * 订单列表统计
     *
     * @return
     */

    MyRespBundle<ConstructionOrderManageVo> getOrderNum();


    /**
     * 指派施工人员列表
     *
     * @return
     */
    MyRespBundle<Map<String, List<appointWorkerListVo>>> appointWorkerList(String companyId);


    /**
     * 项目分配施工人员 （单选）
     * @return
     */
    MyRespBundle<String> appointWorker(String orderNo,String projdectNo,String workerNo,String roleName);
}
