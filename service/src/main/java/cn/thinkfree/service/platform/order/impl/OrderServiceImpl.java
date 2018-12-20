package cn.thinkfree.service.platform.order.impl;

import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.DesignerOrderMapper;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.DesignerOrder;
import cn.thinkfree.database.model.DesignerOrderExample;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.model.UserRegisterExample;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.order.OrderService;
import cn.thinkfree.service.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xusonghui
 */
@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DesignerOrderMapper designerOrderMapper;

    @Override
    public List<Map<String, Object>> allState() {
        List<Map<String, Object>> designOrderStates = DesignStateEnum.allStates(1);
        List<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0; i < designOrderStates.size(); i++) {
            Map<String, Object> state = new HashMap<>();
            state.put("key", designOrderStates.get(i).get("key"));
            state.put("val", "设计订单--" + designOrderStates.get(i).get("val"));
            maps.add(state);
        }
        List<Map<String, Object>> conOrderStates = ConstructionStateEnum.allStates(1);
        for (int i = 0; i < conOrderStates.size(); i++) {
            Map<String, Object> state = new HashMap<>();
            state.put("key", conOrderStates.get(i).get("key"));
            state.put("val", "装饰订单--" + conOrderStates.get(i).get("val"));
            maps.add(state);
        }
        return maps;
    }

    @Override
    public Object getDesignDetail(String orderNo) {
        DesignerOrderExample orderExample = new DesignerOrderExample();
        orderExample.createCriteria().andOrderNoEqualTo(orderNo);
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(orderExample);
        if(designerOrders.isEmpty()){
            throw new RuntimeException("没有查询到订单信息");
        }
        List<Map<String,Object>> stateMaps = DesignStateEnum.allState(0);

        return null;
    }
}
