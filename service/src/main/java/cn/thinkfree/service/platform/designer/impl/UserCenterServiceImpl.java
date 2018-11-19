package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.database.mapper.EmployeeMsgMapper;
import cn.thinkfree.database.model.EmployeeMsg;
import cn.thinkfree.database.model.EmployeeMsgExample;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.utils.HttpUtils;
import cn.thinkfree.service.utils.ReflectUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xusonghui
 */
@Service
public class UserCenterServiceImpl implements UserCenterService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    /**
     * 用户中心地址接口
     */
    private static String userCenterUrl = "http://10.240.10.169:5000/userapi/other/api/user/getListUserByUserIds";
    /**
     * 查询用户的用户中心地址
     */
    private static String queryUserUrl = "http://10.240.10.169:5000/userapi/other/getUserMsgByPhone";
    /**
     * 业主注册
     */
    private static String registerC = "http://10.240.10.169:5000/userapi/register/api/registerC";
    /**
     * 员工注册
     */
    private static String registerS = "http://10.240.10.169:5000/userapi/register/api/registerS";
    /**
     * 根据手机号和姓名模糊查询用户信息
     */
    private static String queryUserByPhoneAndName = "http://10.240.10.169:5000/userapi/other/getUserMsgByPhoneAndName";

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
//        for(String userId : userIds){
//            userMsgVos.add(new UserMsgVo(userId, "测试", "13241229115", "CC", "测试", ""));
//        }
        userMsgVos.addAll(queryUserMsg(employeeMsgMap));
        return userMsgVos;
    }

    @Override
    public UserMsgVo registerUser(String userName, String userPhone, boolean isOwner) {
        UserMsgVo userMsgVo = queryByPhone(userPhone);
        if(userMsgVo == null){
            userMsgVo = register(userName, userPhone, isOwner);
        }
        return userMsgVo;
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

    /**
     * 注册用户
     * @param userName
     * @param userPhone
     * @param isOwner
     */
    private static UserMsgVo register(String userName, String userPhone, boolean isOwner){
        if(isOwner){
            return registerC(userName,userPhone);
        }else{
            return registerS(userName,userPhone);
        }
    }

    private static UserMsgVo registerC(String userName, String userPhone){
        Map<String,String> params = new HashMap<>();
        params.put("userName",userName);
        params.put("userPhone",userPhone);
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(registerC, HttpUtils.mapToParams(params));
        if (httpRespMsg.getResponseCode() != 200) {
            //用户中心服务异常
            throw new RuntimeException("用户中心异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpRespMsg.getContent());
        if (!"1000".equals(jsonObject.getString("code"))) {
            throw new RuntimeException(jsonObject.getString("msg"));
        }
        JSONObject msgs = jsonObject.getJSONObject("data");
        JSONObject user = msgs.getJSONObject(userPhone);
        if(user == null){
            throw new RuntimeException("注册失败");
        }
        UserMsgVo msgVo = new UserMsgVo();
        msgVo.setStaffId(user.getString("staffId"));
        msgVo.setConsumerId(user.getString("consumerId"));
        msgVo.setUserName(userName);
        msgVo.setUserPhone(userPhone);
        return msgVo;
    }

    private static UserMsgVo registerS(String userName, String userPhone){
        Map<String,String> params = new HashMap<>();
        params.put("userName",userName);
        params.put("userPhone",userPhone);
        List<Map<String,String>> mapList = new ArrayList<>();
        mapList.add(params);
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.postJson(registerS, JSONObject.toJSONString(mapList));
        if (httpRespMsg.getResponseCode() != 200) {
            //用户中心服务异常
            throw new RuntimeException("用户中心异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpRespMsg.getContent());
        if (!"1000".equals(jsonObject.getString("code"))) {
            throw new RuntimeException(jsonObject.getString("msg"));
        }
        JSONObject msgs = jsonObject.getJSONObject("data");
        JSONObject user = msgs.getJSONObject(userPhone);
        if(user == null){
            throw new RuntimeException("注册失败");
        }
        UserMsgVo msgVo = new UserMsgVo();
        msgVo.setStaffId(user.getString("staffId"));
        msgVo.setConsumerId(user.getString("consumerId"));
        msgVo.setUserName(userName);
        msgVo.setUserPhone(userPhone);
        return msgVo;
    }

    /**
     * 根据手机号查询用户信息
     * @param userPhone
     */
    private static UserMsgVo queryByPhone(String userPhone){
        Map<String,String> queryUserParams = new HashMap<>();
        queryUserParams.put("phone",userPhone);
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(queryUserUrl, HttpUtils.mapToParams(queryUserParams));
        if (httpRespMsg.getResponseCode() != 200) {
            //用户中心服务异常
            throw new RuntimeException("用户中心异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(httpRespMsg.getContent());
        if (!"1000".equals(jsonObject.getString("code"))) {
            throw new RuntimeException(jsonObject.getString("msg"));
        }
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if(jsonArray.size() < 1){
            return null;
        }
        JSONObject msgObj = jsonArray.getJSONObject(0);
        UserMsgVo msgVo = new UserMsgVo();
        msgVo.setUserName(msgObj.getString("nickName"));
        msgVo.setUserPhone(userPhone);
        msgVo.setUserIcon(msgObj.getString("headPortraits"));
        msgVo.setConsumerId(msgObj.getString("consumerId"));
        msgVo.setStaffId(msgObj.getString("staffId"));
        return msgVo;
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
    @Override
    public List<UserMsgVo> queryUserMsg(String userMsg) {
        long phone;
        try{
            phone = Long.parseLong(userMsg);
        }catch (Exception e){
            phone = -1;
        }
        Map<String,String> queryUserParams = new HashMap<>();
        if(phone > 0){
            queryUserParams.put("phone",userMsg);
        }else {
            queryUserParams.put("nickName",userMsg);
        }
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(queryUserByPhoneAndName, HttpUtils.mapToParams(queryUserParams));
        if (httpRespMsg.getResponseCode() != 200) {
            //用户中心服务异常
            return new ArrayList<>();
        }
        JSONObject jsonObject = JSONObject.parseObject(httpRespMsg.getContent());
        if (!"1000".equals(jsonObject.getString("code"))) {
            return new ArrayList<>();
        }
        JSONArray array = jsonObject.getJSONArray("data");
        List<UserMsgVo> userMsgVos = new ArrayList<>();
        for (int i = 0 ; i < array.size() ; i ++) {
            JSONObject userObj = array.getJSONObject(i);
            String userName = userObj.getString("nickName");
            String userPhone = userObj.getString("phone");
            String headPortraits = userObj.getString("headPortraits");
            String consumerId = userObj.getString("consumerId");
            if(StringUtils.isBlank(consumerId)){
                continue;
            }
            userMsgVos.add(new UserMsgVo(consumerId, userName, userPhone, "CC", "", headPortraits));
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
            if(StringUtils.isBlank(employeeMsg.getRoleCode())){
                employeeMsg.setRoleCode("CC");
            }
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
