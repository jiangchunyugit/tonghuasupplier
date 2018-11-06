package cn.thinkfree.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "url")
public class HttpLinks {
    /**
     * 获取用户信息链接
     */
    private String userCenterGetUserMsg;
}
