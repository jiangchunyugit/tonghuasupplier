package cn.thinkfree.service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/24 18:02
 */
@Data
@Component
public class UserCenterConfig {
    @Value("${user.center.url.get-user-msg}")
    private String getUserMsgUrl;
}
