package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.service.platform.designer.UserService;
import cn.thinkfree.service.platform.designer.vo.UserMsgVo;
import cn.thinkfree.service.utils.ReflectUtils;
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
    @Override
    public List<UserMsgVo> queryUsers(List<String> userIds) {
        List<UserMsgVo> userMsgVos = new ArrayList<>();
        for(String userId : userIds){
            userMsgVos.add(new UserMsgVo(userId,"测试名称","13241229115","CC"));
        }
        return userMsgVos;
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
