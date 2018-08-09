package cn.thinkfree.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 *  需要反转的入参
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(PARAMETER)
public @interface AppParameter {
    boolean required() default true;

}
