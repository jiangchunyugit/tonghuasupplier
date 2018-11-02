package cn.thinkfree.service.utils;

import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.vo.AfUserDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/11/1 18:23
 */

public class AfUtils {

    public static AfUserDTO getUserInfo(String url, String userId, String roleId) {
        Map<String, String> requestMap = new HashMap<>(2);
        requestMap.put("userId", userId);
        requestMap.put("roleId", roleId);
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(url, requestMap);
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
