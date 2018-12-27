package cn.thinkfree.service.platform.update.impl;

import cn.thinkfree.database.mapper.AppUpdateLogMapper;
import cn.thinkfree.database.model.AppUpdateLog;
import cn.thinkfree.database.model.AppUpdateLogExample;
import cn.thinkfree.service.platform.update.AppUpdateService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 */
@Service
public class AppUpdateServiceImpl implements AppUpdateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppUpdateLogMapper appUpdateLogMapper;

    @Override
    public Map<String, Object> getVersionMsg() {
        Map<String, Object> objectMap = new HashMap<>();
        try{
            objectMap.put("androidVesionMsg", getAndroidVersionMsg());
            objectMap.put("iosVersionMsg", getIosVersionMsg());
        }catch (Exception e){
            logger.error("获取版本信息失败：",e);
        }
        return objectMap;
    }

    private Map<String, Object> getAndroidVersionMsg() {
        AppUpdateLogExample appUpdateLogExample = new AppUpdateLogExample();
        appUpdateLogExample.createCriteria().andAppTypeEqualTo("android").andEffectTimeLessThanOrEqualTo(new Date());
        appUpdateLogExample.setOrderByClause(" effect_time desc limit 1");
        List<AppUpdateLog> appUpdateLogs = appUpdateLogMapper.selectByExample(appUpdateLogExample);
        if (appUpdateLogs.isEmpty()) {
            return new HashMap<>();
        }
        AppUpdateLog androidLog = appUpdateLogs.get(0);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("minVersion", androidLog.getMinVersion());
        objectMap.put("nowVersion", androidLog.getNowVersion());
        objectMap.put("apkUrl", androidLog.getUpdateUrl());
        objectMap.put("updateDescription", androidLog.getDescription());
        objectMap.put("forceUpdateVersions", JSONObject.parseArray(androidLog.getForceUpdateVersions()));
        return objectMap;
    }

    private Map<String, Object> getIosVersionMsg() {
        AppUpdateLogExample appUpdateLogExample = new AppUpdateLogExample();
        appUpdateLogExample.createCriteria().andAppTypeEqualTo("ios").andEffectTimeLessThanOrEqualTo(new Date());
        appUpdateLogExample.setOrderByClause(" effect_time desc limit 1");
        List<AppUpdateLog> appUpdateLogs = appUpdateLogMapper.selectByExample(appUpdateLogExample);
        if (appUpdateLogs.isEmpty()) {
            return new HashMap<>();
        }
        AppUpdateLog iosLog = appUpdateLogs.get(0);
        Map<java.lang.String, java.lang.Object> objectMap = new HashMap<>();
        objectMap.put("minVersion", iosLog.getMinVersion());
        objectMap.put("nowVersion", iosLog.getNowVersion());
        objectMap.put("iosUrl", iosLog.getUpdateUrl());
        objectMap.put("updateDescription", iosLog.getDescription());
        objectMap.put("forceUpdateVersions", JSONObject.parseArray(iosLog.getForceUpdateVersions()));
        return objectMap;
    }
}
