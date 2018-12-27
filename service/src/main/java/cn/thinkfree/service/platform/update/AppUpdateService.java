package cn.thinkfree.service.platform.update;

import java.util.Map;

/**
 * @author xusonghui
 *
 */
public interface AppUpdateService {
    /**
     * 获取版本信息
     * @return
     */
    Map<String,Object> getVersionMsg(String appType);
}
