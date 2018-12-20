package cn.thinkfree.service.construction.impl;

import cn.thinkfree.database.mapper.ConstructionOrderPayMapper;
import cn.thinkfree.database.model.ConstructionOrderPay;
import cn.thinkfree.database.model.ConstructionOrderPayExample;
import cn.thinkfree.service.construction.ConstructOrderPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 施工订单支付服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/13 17:21
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ConstructOrderPayServiceImpl implements ConstructOrderPayService {

    @Autowired
    private ConstructionOrderPayMapper constructionOrderPayMapper;


    @Override
    public ConstructionOrderPay findByOrderNo(String orderNo) {
        ConstructionOrderPayExample example = new ConstructionOrderPayExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrderPay> constructionOrderPays = constructionOrderPayMapper.selectByExample(example);
        return constructionOrderPays != null && constructionOrderPays.size() > 0 ? constructionOrderPays.get(0) : null;
    }

    @Override
    public void updateByOrderNo(ConstructionOrderPay constructionOrderPay, String orderNo) {
        ConstructionOrderPayExample example = new ConstructionOrderPayExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        constructionOrderPayMapper.updateByExampleSelective(constructionOrderPay, example);
    }

    @Override
    public void insert(ConstructionOrderPay constructionOrderPay) {
        constructionOrderPayMapper.insertSelective(constructionOrderPay);
    }
}
