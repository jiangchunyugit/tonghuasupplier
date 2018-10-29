package cn.thinkfree.service.platform.designer;

import cn.thinkfree.service.platform.designer.vo.UserMsgVo;

import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 获取用户信息服务即可
 */
public interface UserCenterService {
    /**
     * 根据用户ID查询用户信息
     *
     * @param userIds 用户ID列表
     * @return
     */
    List<UserMsgVo> queryUsers(List<String> userIds);

    /**
     * 根据用户id查询用户信息
     *
     * @param userIds
     * @return
     */
    Map<String, UserMsgVo> queryUserMap(List<String> userIds);

    /**
     * 根据用户ID查询用户
     *
     * @param userId
     * @return
     */
    UserMsgVo queryUser(String userId);
}
