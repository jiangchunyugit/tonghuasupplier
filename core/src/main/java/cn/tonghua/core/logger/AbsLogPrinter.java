package cn.tonghua.core.logger;

import cn.tonghua.core.base.MyLogger;
import cn.tonghua.core.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志打印器
 *
 */
public abstract class AbsLogPrinter {
    MyLogger logger = LogUtil.getLogger(getClass());


    public void printInfoMes(String format, Object... obj) {
        logger.info(format,obj);
    }


    public void printErrorMes(String format, Object... obj) {
        logger.error(format,obj);
    }


    public void printWarnMes(String format, Object... obj) {
        logger.warn(format,obj);
    }


    public void printDebugMes(String format, Object... obj) {
        logger.debug(format,obj);
    }

}
