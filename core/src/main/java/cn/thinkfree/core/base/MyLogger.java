package cn.thinkfree.core.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于限定log
 */
public class MyLogger {



    private Logger logger = null;

    public MyLogger(){
        this.logger = LoggerFactory.getLogger(getClass());
    }
    public MyLogger(Class cls){
        this.logger = LoggerFactory.getLogger(cls);
    }
    public MyLogger(String clsName){
        this.logger = LoggerFactory.getLogger(clsName);
    }

    public void warn(String mark,Object ... obj){
       getLogger().warn(mark,obj);
    }

    public void warn(String mark,Throwable throwable){
        getLogger().warn(mark, throwable);
    }

    public void info(String mark,Object ... obj){
        getLogger().info(mark, obj);
    }
    public void info(String mark,Throwable throwable){
        getLogger().info(mark, throwable);
    }

    public void error(String mark,Object ... obj){
        getLogger().error(mark,obj);
    }
    public void error(String mark,Throwable throwable){
        getLogger().error(mark, throwable);
    }
    public void debug(String mark,Object ... obj){
        getLogger().debug(mark, obj);
    }
    public void debug(String mark,Throwable throwable){
        getLogger().debug(mark, throwable);
    }
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
