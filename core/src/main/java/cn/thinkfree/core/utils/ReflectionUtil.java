package cn.thinkfree.core.utils;


import cn.thinkfree.core.base.MyLogger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class ReflectionUtil {

    static MyLogger logger = LogUtil.getLogger(ReflectionUtil.class);

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * 
     * 如public UserDao extends HibernateDao<User,Long>
     * 
     * @param clazz
     *            clazz The class to introspect

     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(final Class clazz) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.debug(clazz.getSimpleName()
                    + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        return (Class) params[0];
    }
}