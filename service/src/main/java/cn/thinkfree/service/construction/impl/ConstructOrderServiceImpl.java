package cn.thinkfree.service.construction.impl;

import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.ConstructOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 施工订单服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/13 17:01
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ConstructOrderServiceImpl implements ConstructOrderService {

    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;


    @Override
    public ConstructionOrder findByProjectNo(String projectNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(example);
        return constructionOrders != null && constructionOrders.size() > 0 ? constructionOrders.get(0) : null;
    }
}
