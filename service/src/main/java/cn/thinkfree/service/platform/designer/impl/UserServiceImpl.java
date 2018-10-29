package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.database.mapper.EmployeeMsgMapper;
import cn.thinkfree.database.model.EmployeeMsg;
import cn.thinkfree.database.model.EmployeeMsgExample;
import cn.thinkfree.service.platform.designer.UserService;
import cn.thinkfree.service.platform.designer.vo.UserMsgVo;
import cn.thinkfree.service.utils.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;

    @Override
    public List<UserMsgVo> queryUsers(List<String> userIds) {
        List<UserMsgVo> userMsgVos = new ArrayList<>();
        Map<String,EmployeeMsg> employeeMsgMap = queryEmployeeMsg(userIds);
        for(String userId : userIds){
            EmployeeMsg employeeMsg = employeeMsgMap.get(userId);
            if(employeeMsg == null){
                userMsgVos.add(new UserMsgVo(userId,"测试名称","13241229115","CC"));
            }else {
                userMsgVos.add(new UserMsgVo(userId,"测试名称","13241229115","CC", employeeMsg.getRealName()));
            }
        }
        return userMsgVos;
    }

    /**
     * 根据员工ID查询员工信息
     * @param userIds
     * @return
     */
    private Map<String,EmployeeMsg> queryEmployeeMsg(List<String> userIds){
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdIn(userIds);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        return ReflectUtils.listToMap(employeeMsgs,"userId");
    }

    @Override
    public Map<String, UserMsgVo> queryUserMap(List<String> userIds) {
        List<UserMsgVo> msgVos = queryUsers(userIds);
        return ReflectUtils.listToMap(msgVos,"userId");
    }

    @Override
    public UserMsgVo queryUser(String userId) {
        List<UserMsgVo> msgVos = queryUsers(Arrays.asList(userId));
        return msgVos.get(0);
    }
}
