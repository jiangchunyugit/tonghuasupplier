package cn.tonghua.core.annotation;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 *  需要反转的出参
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
@CrossOrigin(origins = "*",allowedHeaders = {"Content-Type","XFILENAME","XFILECATEGORY","XFILESIZE","x-requested-with","Authorization"})
public @interface MyRespBody {
}
