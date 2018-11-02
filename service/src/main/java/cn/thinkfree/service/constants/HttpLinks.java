package cn.thinkfree.service.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * http链接常量
 *
 * @author song
 * @version 1.0
 * @date 2018/10/23 16:40
 */
@Data
@Component
public class HttpLinks {
    /**
     * 获取用户信息链接
     */
    @Value("${user.center.url.get-user-msg}")
    private String userCenterGetUserMsgUrl;
}
