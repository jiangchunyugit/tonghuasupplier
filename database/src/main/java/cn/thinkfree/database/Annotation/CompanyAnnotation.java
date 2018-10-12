package cn.thinkfree.database.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanyAnnotation {
	/** * 公司ID(暂未启用) * @return */
	String CompanyId() default "";

	/** * 别名（针对复杂查询备用处理属性） * @return */
	String Alias() default "";
}
