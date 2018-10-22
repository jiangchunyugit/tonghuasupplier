package cn.thinkfree.service.neworder;

import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.service.constants.Scheduling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单相关
 * @author gejiaming
 */
@Service
public class NewOrderServiceImpl implements NewOrderService {
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;

    /**
     * 获取订单信息
     * @param projectNo
     * @return
     */
    @Override
    public ConstructionOrder getConstructionOrder(String projectNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        ConstructionOrderExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(example);
        return constructionOrders.get(0);
    }
}
