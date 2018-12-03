package cn.thinkfree.service.platform.order.impl;

import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.service.platform.order.OrderStateService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xusonghui
 */
@Service
public class OrderStateServiceImpl implements OrderStateService {

    @Override
    public List<Map<String, Object>> allState() {
        List<Map<String, Object>> designOrderStates = DesignStateEnum.allStates(1);
        List<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0; i < designOrderStates.size(); i++) {
            Map<String,Object> state = new HashMap<>();
            state.put("key",designOrderStates.get(i).get("key"));
            state.put("val","设计订单--" + designOrderStates.get(i).get("val"));
            maps.add(state);
        }
        List<Map<String, Object>> conOrderStates = ConstructionStateEnumB.allStates(1);
        for (int i = 0; i < conOrderStates.size(); i++) {
            Map<String,Object> state = new HashMap<>();
            state.put("key",conOrderStates.get(i).get("key"));
            state.put("val","装饰订单--" + conOrderStates.get(i).get("val"));
            maps.add(state);
        }
        return maps;
    }
}
