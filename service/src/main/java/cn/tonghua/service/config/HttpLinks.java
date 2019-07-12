package cn.tonghua.service.config;

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
    /**
     * 发送变更单金额
     */
    private String createFee;
    /**
     * 发送审批消息
     */
    private String messageSave;
    /**
     * 项目支付信息
     */
    private String projectPayInfo;
}
