package cn.thinkfree.core.annotation;

import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;


/**
 * 系统日志
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface MySysLog {




    /**
     * 描述
     * @return
     */
    String desc() default "";

    /**
     * 行为
     * @return
     */
    SysLogAction action();

    /**
     * 模块
     * @return
     */
    SysLogModule module();


}
