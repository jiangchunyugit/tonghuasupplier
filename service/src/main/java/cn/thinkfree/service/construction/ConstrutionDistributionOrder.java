package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.vo.DistributionOrderCityVo;

import java.util.List;

public interface ConstrutionDistributionOrder {


    /**
     *  施工派单-公司/城市列表接口（含搜索公司）
     * @param companyName
     * @return
     */
    MyRespBundle<List<DistributionOrderCityVo>> getCityList(String companyName);


    /**
     *  施工派单- 确认派单
     * @param companyId
     * @return
     */
    MyRespBundle<String> DistributionCompany(String orderNo, String companyId);
}
