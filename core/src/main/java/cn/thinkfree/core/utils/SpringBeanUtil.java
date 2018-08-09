package cn.thinkfree.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by lenovo on 2016/12/16.
 */
public class SpringBeanUtil  extends BeanUtils {
    private static final ConcurrentMap<String, HashMap<String, String>> fieldNameCache = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(SpringBeanUtil.class);

    /**
     *  拷贝属性（忽略类型不一致而名称相同的属性）
     */
    public static void copy(Object source, Object target) {
        copy(source, target, false);
    }

    /**
     * 拷贝属性（忽略类型不一致的属性），指定ignoreNull参数是否忽略为null的属性
     */
    public static void copy(Object source, Object target, boolean ignoreNull) {
        BeanWrapper sourceBean = new BeanWrapperImpl(source);
        BeanWrapper targetBean = new BeanWrapperImpl(target);
        PropertyDescriptor[] targetProperties = getPropertyDescriptors(target.getClass());
        for (PropertyDescriptor property : targetProperties) {
            Class<?> targetType = property.getPropertyType();
            String name = property.getName();
            PropertyDescriptor sourceProperty = getPropertyDescriptor(source.getClass(), name);
            if (sourceProperty == null)
                continue;
            Object propertyVal = sourceBean.getPropertyValue(property.getName());
            if (!targetType.equals(sourceProperty.getPropertyType()) || "class".equals(property.getName())
                    || propertyVal == null || (ignoreNull && propertyVal == null))
                continue;
            targetBean.setPropertyValue(property.getName(), sourceBean.getPropertyValue(property.getName()));
        }
    }

    /**
     * 获取目标对象的所有属性列表
     * return : String[]
     */
    public static Map<String, String> getFieldNames(Object target) {
        if (target == null)
            return null;
        HashMap<String, String> fieldNames  = fieldNameCache.get(target.getClass().getName());
        if (!CollectionUtils.isEmpty(fieldNames))
            return fieldNames;
        fieldNames = new HashMap<>();
        PropertyDescriptor[] targetProperties = getPropertyDescriptors(target.getClass());
        for (PropertyDescriptor property : targetProperties) {
            if ("class".equals(property.getName()))
                continue;
            fieldNames.put(property.getName(), property.getName());
        }
        fieldNameCache.putIfAbsent(target.getClass().getName(), fieldNames);
        return fieldNames;
    }

    /**
     * Description : 将对象转为Map
     * return : Map<String,Object>
     */
    public static Map<String, Object> toMap(Object target) {
        return toMap(target, null, false);
    }

    public static Map<String, Object> toMap(Object target, boolean isNull) {
        return toMap(target, null, isNull);
    }

    /**
     * Description : 将目标对象target转为Map格式,但将excludeProperties集合指定的属性排除在外
     * return : Map<String,Object>
     */
    public static Map<String, Object> toMap(Object target, List<String> excludeProperties, boolean isNull) {
        if (target == null)
            throw new RuntimeException("对象不能为空！");
        BeanWrapper bean = new BeanWrapperImpl(target);
        PropertyDescriptor[] targetProperties = BeanUtils.getPropertyDescriptors(target.getClass());
        Map<String, Object> map = new HashMap<>();
        for (PropertyDescriptor property : targetProperties) {
            Object propetyValue = bean.getPropertyValue(property.getName());
            if (isNull && propetyValue == null) {
                map.put(property.getName(), "");
                continue;
            }
            if (propetyValue == null || isExcludeProperty(excludeProperties, property.getName()))
                continue;
            if (propetyValue instanceof String) { // 处理字符串类型
                if (StringUtils.hasText(propetyValue.toString())) {
                    propetyValue = propetyValue.toString().trim();
                } else {
                    continue;
                }
            }
            map.put(property.getName(), propetyValue);
        }
        return map;
    }

    /**
     * Description : 是否排除该属性
     * return : boolean
     */
    private static boolean isExcludeProperty(List<String> excludeProperties, String propertyName) {

        if ("class".equals(propertyName)) // 如果当前属性名称等于class,则排除
            return true;

        if (CollectionUtils.isEmpty(excludeProperties))
            return false;

        for (String property : excludeProperties) { // 如果当前属性名称在可排除集合之内,则返回true
            if (property.equals(propertyName))
                return true;
        }
        return false;
    }

    /**
     * @Description : 判断一个对象是否为空；首先这个对象不能空,并且对象中的属性至少有一个不为空,则认为这个对象不为空，返回true,
     *              否则返回false
     * @return : boolean
     *
     */
    public static boolean isEmpty(Object target) {
        return !isNotEmpty(target);
    }

    public static boolean isNotEmpty(Object target) {
        if (target == null) {
            return false;
        }
        BeanWrapper targetBean = new BeanWrapperImpl(target);
        PropertyDescriptor[] targetProperties = getPropertyDescriptors(target.getClass());
        for (PropertyDescriptor property : targetProperties) {
            Object propertyVal = targetBean.getPropertyValue(property.getName());
            if ("class".equals(property.getName()))
                continue;
            // 如果属性不为空,且属性类型为字符串类型且为有效字符串时，返回true
            if (propertyVal != null
                    && (propertyVal instanceof String && StringUtils.hasText((CharSequence) propertyVal)))
                return true;
            // 否则属性不为空,返回true,否则返回false
            if (propertyVal != null && !(propertyVal instanceof String))
                return true;
        }
        return false;
    }


}
