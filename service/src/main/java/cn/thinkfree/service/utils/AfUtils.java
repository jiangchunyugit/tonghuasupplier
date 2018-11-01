package cn.thinkfree.service.utils;

import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.vo.AfUserDTO;
import cn.thinkfree.service.constants.HttpLinks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/11/1 18:23
 */
@Component
public class AfUtils {

    @Value("user.center.url.get-user-msg")
    private String userCenterGetUserMsgUrl;

    public static AfUserDTO getUserInfo(String userId, String roleId) {
        Map<String, String> requestMap = new HashMap<>(2);
        requestMap.put("userId", userId);
        requestMap.put("roleId", roleId);
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(HttpLinks.USER_CENTER_GETUSERMSG, requestMap);
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        Map date = (Map) responseMap.get("data");
        String username = (String) date.get("nickName");
        String headPortrait = (String) date.get("headPortraits");
        AfUserDTO userDTO = new AfUserDTO();
        userDTO.setUserId(userId);
        userDTO.setUsername(username);
        userDTO.setHeadPortrait(headPortrait);
        return userDTO;
    }
}
