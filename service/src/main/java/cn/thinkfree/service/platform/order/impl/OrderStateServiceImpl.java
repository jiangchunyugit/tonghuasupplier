package cn.thinkfree.service.platform.order.impl;

import cn.thinkfree.core.constants.ConstructionStateEnumB;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.service.platform.order.OrderStateService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

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
        for (int i = 0; i < designOrderStates.size(); i++) {
            designOrderStates.get(i).put("val", "设计订单--" + designOrderStates.get(i).get("val"));
            System.out.println(designOrderStates.get(i).toString());
        }
        List<Map<String, Object>> conOrderStates = ConstructionStateEnumB.allStates(1);
        for (int i = 0; i < conOrderStates.size(); i++) {
            conOrderStates.get(i).put("val", "装饰订单--" + conOrderStates.get(i).get("val"));
            System.out.println(conOrderStates.get(i).toString());
        }
        designOrderStates.addAll(conOrderStates);
        return designOrderStates;
    }
}
