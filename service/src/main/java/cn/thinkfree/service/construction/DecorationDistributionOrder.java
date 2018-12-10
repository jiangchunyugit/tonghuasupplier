package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.construction.vo.DecorationOrderListVo;
import cn.thinkfree.service.construction.vo.appointWorkerListVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface DecorationDistributionOrder {

    /**
     * 订单列表
     *
     * @return
     */
    PageInfo<DecorationOrderListVo> getOrderList(String companyNo, int pageNum, int pageSize, String projectNo, String appointmentTime,
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
    void appointWorker(List<Map<String,String>> workerInfo);
}
