package cn.thinkfree.service.utils;

import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.vo.AfUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 审批流工具
 *
 * @author song
 * @version 1.0
 * @date 2018/11/1 18:23
 */

public class AfUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfUtils.class);

    /**
     * 获取用户信息
     * @param url 外部url
     * @param userId 用户编号
     * @param roleId 角色编号
     * @return 用户信息
     */
    public static AfUserDTO getUserInfo(String url, String userId, String roleId) {
        Map<String, String> requestMap = new HashMap<>(2);
        requestMap.put("userId", userId);
        requestMap.put("roleId", roleId);
        LOGGER.info("获取用户信息，requestMap：{}", JSONUtil.bean2JsonStr(requestMap));
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(url, requestMap);
        LOGGER.info("获取用户信息， httpRespMsg：{}", JSONUtil.bean2JsonStr(httpRespMsg));
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        LOGGER.info("获取用户信息， responseMap：{}", JSONUtil.bean2JsonStr(responseMap));
        Map date = (Map) responseMap.get("data");
        String username = (String) date.get("nickName");
        String headPortrait = (String) date.get("headPortraits");
        String phone = (String) date.get("phone");
        AfUserDTO userDTO = new AfUserDTO();
        userDTO.setUserId(userId);
        userDTO.setUsername(username);
        userDTO.setHeadPortrait(headPortrait);
        userDTO.setPhone(phone);
        return userDTO;
    }
}
