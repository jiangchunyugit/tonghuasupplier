package cn.thinkfree.service.platform.order.impl;

import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.order.OrderService;
import cn.thinkfree.service.utils.HttpUtils;
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
    private UserCenterService userCenterService;
    /**
     * 发送动态朋友全地址
     */
    private static final String dynamicUrl = "/uAppapi/dynamic/save";

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
        List<Map<String, Object>> conOrderStates = ConstructionStateEnum.allStates(1);
        for (int i = 0; i < conOrderStates.size(); i++) {
            Map<String,Object> state = new HashMap<>();
            state.put("key",conOrderStates.get(i).get("key"));
            state.put("val","装饰订单--" + conOrderStates.get(i).get("val"));
            maps.add(state);
        }
        return maps;
    }

    @Override
    public void sendPredatingMsg(String projectNo, String designerId, String subscribeTime, String remark) {
        String details = "发起了预交底，预交底时间：" + subscribeTime + "。" + remark;
        Map<String, String> params = new HashMap<>();
        params.put("projectNo", projectNo);
        params.put("type", "0");
        params.put("details",details);
        params.put("senderId", designerId);
        try{
            HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(userCenterService.getUrl(dynamicUrl), HttpUtils.mapToParams(params));
            if(httpRespMsg.getResponseCode() != 200){
                logger.error("发送动态失败:", JSONObject.toJSONString(httpRespMsg));
            }else{
                JSONObject object = JSONObject.parseObject(httpRespMsg.getContent());
                if(!"1000".equals(object.getString("code"))){
                    logger.error("发送动态失败:", JSONObject.toJSONString(httpRespMsg));
                }else{
                    logger.info("发送动态成功:", JSONObject.toJSONString(httpRespMsg));
                }
            }
        }catch (Exception e){
            logger.error("发送动态失败:",e);
        }
    }
}
