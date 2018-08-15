package cn.thinkfree.service.system;

import cn.thinkfree.database.model.LogInfo;
import cn.thinkfree.database.vo.LogInfoSEO;
import com.github.pagehelper.PageInfo;

public interface LogInfoService {

    void save(LogInfo logInfo);

    PageInfo<LogInfo> pageLogInfo(LogInfoSEO logInfoSEO);

}
