package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.database.mapper.EmployeeMsgMapper;
import cn.thinkfree.database.model.EmployeeMsg;
import cn.thinkfree.database.model.EmployeeMsgExample;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.utils.HttpUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xusonghui
 */
@Service
public class UserCenterServiceImpl implements UserCenterService {
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    /**
     * 用户中心地址接口
     */
    private static String userCenterUrl = "http://10.240.10.169:5000/userapi/other/api/user/getListUserByUserIds";

    @Override
    public List<UserMsgVo> queryUsers(List<String> userIds) {
        List<UserMsgVo> userMsgVos = new ArrayList<>();
        Map<String, EmployeeMsg> employeeMsgMap = queryEmployeeMsg(userIds);
        for (String userId : userIds) {
            if (!employeeMsgMap.containsKey(userId)) {
                EmployeeMsg employeeMsg = new EmployeeMsg();
                employeeMsg.setRoleCode("CC");
                employeeMsg.setUserId(userId);
                employeeMsgMap.put(userId, employeeMsg);
            }
        }
        userMsgVos.addAll(queryUserMsg(employeeMsgMap));
        return userMsgVos;
    }

    public static void main(String[] args) {
        Map<String, EmployeeMsg> employeeMsgMap = new HashMap<>();
        EmployeeMsg employeeMsg = new EmployeeMsg();
        employeeMsg.setUserId("CC18103016014600009");
        employeeMsg.setRoleCode("CC");
        employeeMsgMap.put("CC18103016014600009", employeeMsg);
        List<UserMsgVo> userMsgVos = queryUserMsg(employeeMsgMap);
        System.out.println(JSONObject.toJSONString(userMsgVos));
    }

    private static List<UserMsgVo> queryUserMsg(Map<String, EmployeeMsg> employeeMsgMap) {
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.postJson(userCenterUrl, getParams(employeeMsgMap));
        if (httpRespMsg.getResponseCode() != 200) {
            //用户中心服务异常
            throw new RuntimeException("用户中心异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpRespMsg.getContent());
        if (!"1000".equals(jsonObject.getString("code"))) {
            throw new RuntimeException("无效的用户ID");
        }
        JSONObject dataObj = jsonObject.getJSONObject("data");
        List<UserMsgVo> userMsgVos = new ArrayList<>();
        for (Map.Entry<String, EmployeeMsg> msgEntry : employeeMsgMap.entrySet()) {
            EmployeeMsg employeeMsg = msgEntry.getValue();
            String userId = employeeMsg.getUserId();
            if (!dataObj.containsKey(userId)) {
                continue;
            }
            JSONObject userMsg = dataObj.getJSONObject(userId);
            String userName = userMsg.getString("nickName");
            String phone = userMsg.getString("phone");
            String headPortraits = userMsg.getString("headPortraits");
            userMsgVos.add(new UserMsgVo(userId, userName, phone, employeeMsg.getRoleCode(), employeeMsg.getRealName(), headPortraits));
        }
        return userMsgVos;
    }

    /**
     * 参数转换
     *
     * @param employeeMsgMap
     * @return
     */
    private static String getParams(Map<String, EmployeeMsg> employeeMsgMap) {
        List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, EmployeeMsg> msgEntry : employeeMsgMap.entrySet()) {
            EmployeeMsg employeeMsg = msgEntry.getValue();
            Map<String, String> params = new HashMap<>();
            params.put("userId", employeeMsg.getUserId());
            params.put("roleId", employeeMsg.getRoleCode());
            list.add(params);
        }
        return JSONObject.toJSONString(list);
    }

    /**
     * 根据员工ID查询员工信息
     *
     * @param userIds
     * @return
     */
    private Map<String, EmployeeMsg> queryEmployeeMsg(List<String> userIds) {
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdIn(userIds);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        return ReflectUtils.listToMap(employeeMsgs, "userId");
    }

    @Override
    public Map<String, UserMsgVo> queryUserMap(List<String> userIds) {
        List<UserMsgVo> msgVos = queryUsers(userIds);
        return ReflectUtils.listToMap(msgVos, "userId");
    }

    @Override
    public UserMsgVo queryUser(String userId) {
        List<UserMsgVo> msgVos = queryUsers(Arrays.asList(userId));
        if (msgVos.isEmpty()) {
            throw new RuntimeException("没有查询到用户信息");
        }
        return msgVos.get(0);
    }
}
