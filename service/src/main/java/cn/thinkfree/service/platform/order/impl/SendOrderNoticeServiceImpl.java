package cn.thinkfree.service.platform.order.impl;

import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.model.UserRegisterExample;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.order.SendOrderNoticeService;
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
public class SendOrderNoticeServiceImpl implements SendOrderNoticeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserCenterService userCenterService;
    /**
     * 发送动态朋友全地址
     */
    private static final String dynamicUrl = "/uAppapi/dynamic/save";
    /**
     * 发送友盟推送地址
     */
    private static final String youmengMsgUrl = "/uAppapi/message/save";
    /**
     * 发送邮件地址
     */
    private static final String emailMsgUrl = "/uAppapi/mail/send";
    @Autowired
    private UserRegisterMapper registerMapper;

    @Override
    public void sendPredatingMsg(String projectNo, String designerId, String subscribeTime, String remark) {
        String details = "发起了预交底，预交底时间：" + subscribeTime + "。" + remark;
        Map<String, String> params = new HashMap<>();
        params.put("projectNo", projectNo);
        params.put("type", "0");
        params.put("details", details);
        params.put("senderId", designerId);
        logger.info("开始发送动态消息：", JSONObject.toJSONString(params));
        sendHttp(params, dynamicUrl);
    }

    private void sendHttp(Map<String, String> params, String url) {
        try {
            HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(userCenterService.getUrl(url), HttpUtils.mapToParams(params));
            if (httpRespMsg.getResponseCode() != 200) {
                logger.error("发送失败:", JSONObject.toJSONString(httpRespMsg));
            } else {
                JSONObject object = JSONObject.parseObject(httpRespMsg.getContent());
                if (!"1000".equals(object.getString("code"))) {
                    logger.error("发送失败:", JSONObject.toJSONString(httpRespMsg));
                } else {
                    logger.info("发送成功:", JSONObject.toJSONString(httpRespMsg));
                }
            }
        } catch (Exception e) {
            logger.error("发送动态失败:", e);
        }
    }

    private void sendNoticeMsg(String userId, String projectNo, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("projectNo", projectNo);
        params.put("type", "0");
        params.put("userNo", userId);
        params.put("content", content);
        params.put("dynamicId", "0");
        params.put("type", "5");
        logger.info("开始发送推送消息：", JSONObject.toJSONString(params));
        sendHttp(params, youmengMsgUrl);
    }

    private void sendNoticeEmail(String emailAddress, String templateNo, Map<String,String> tplContent) {
        Map<String, String> params = new HashMap<>();
        params.put("recipientAddress", emailAddress);
        params.put("templateNo", templateNo);
        params.put("tplContent", JSONObject.toJSONString(tplContent));
        logger.info("开始发送推送消息：", JSONObject.toJSONString(params));
        sendHttp(params, emailMsgUrl);
    }

    @Override
    public void sendDesignerReceipt(String ownerId, String projectNo) {
        String content = "您的项目编号为[" + projectNo + "]的项目已经被设计师接单。";
        sendNoticeMsg(ownerId, projectNo, content);
    }

    @Override
    public void sendEmployeeReceipt(String userId, String projectNo) {
        String content = "您有新的订单，项目编号为[" + projectNo + "]，请去项目列表查看。";
        sendNoticeMsg(userId, projectNo, content);
    }

    @Override
    public void sendCompanyDispatch(String designerId, String projectNo) {
        String content = "您有新的订单，请及时处理，项目编号为[" + projectNo + "]。";
        sendNoticeMsg(designerId, projectNo, content);
    }

    @Override
    public void sendPlatformDispatch(String companyId, String projectNo, String orderTypeName) {
        List<UserRegister> userRegisters = getUserRegisters(companyId);
        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("projectNo", projectNo);
        stringMap.put("orderTypeName", orderTypeName);
        for(UserRegister userRegister : userRegisters){
            sendNoticeEmail(userRegister.getPhone(), "005", stringMap);
        }
    }

    private List<UserRegister> getUserRegisters(String companyId) {
        UserRegisterExample userRegisterExample = new UserRegisterExample();
        userRegisterExample.createCriteria().andUserIdEqualTo(companyId);
        List<UserRegister> userRegisters = registerMapper.selectByExample(userRegisterExample);
        if(userRegisters.isEmpty()){
            return new ArrayList<>();
        }
        return userRegisters;
    }

    @Override
    public void sendDesignerNoReceipt(String companyId, String projectNo, String designerName) {
        List<UserRegister> userRegisters = getUserRegisters(companyId);
        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("projectNo", projectNo);
        stringMap.put("designerName", designerName);
        for(UserRegister userRegister : userRegisters){
            sendNoticeEmail(userRegister.getPhone(), "006", stringMap);
        }
    }

    @Override
    public void sendOwnerConsDispatch(String ownerId, String projectNo) {
        String content = "您的项目编号为[" + projectNo + "]的项目已经指派了装饰公司。";
        sendNoticeMsg(ownerId, projectNo, content);
    }
}
