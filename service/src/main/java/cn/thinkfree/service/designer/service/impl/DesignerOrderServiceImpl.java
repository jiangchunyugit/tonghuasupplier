package cn.thinkfree.service.designer.service.impl;

import cn.thinkfree.database.mapper.DesignerOrderMapper;
import cn.thinkfree.database.model.DesignerOrder;
import cn.thinkfree.database.model.DesignerOrderExample;
import cn.thinkfree.service.designer.service.DesignerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设计订单服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 16:56
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DesignerOrderServiceImpl implements DesignerOrderService {

    @Autowired
    private DesignerOrderMapper designerOrderMapper;


    @Override
    public DesignerOrder findByProjectNo(String projectNo) {
        DesignerOrderExample example = new DesignerOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(example);
        return designerOrders != null && designerOrders.size() > 0 ? designerOrders.get(0) : null;
    }
}
