package cn.tonghua.core.utils;


import cn.tonghua.core.base.MyLogger;

/**
 * Created by lenovo on 2017/5/5.
 */
public class LogUtil {

    public static MyLogger getLogger(Class cls){
        return new MyLogger(cls);
    }

}
