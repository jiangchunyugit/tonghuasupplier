package cn.thinkfree.service.system;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.model.LogInfo;
import cn.thinkfree.database.vo.LogInfoSEO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

public class LogInfoServiceImpl implements LogInfoService {

    MyLogger myLogger = LogUtil.getLogger(LogInfoService.class);
    @Override
    public void save(LogInfo logInfo) {

    }

    @Transactional
    @Override
    public PageInfo<LogInfo> pageLogInfo(LogInfoSEO logInfoSEO) {

        PageHelper.offsetPage(20,20);

        return null;
    }
}
