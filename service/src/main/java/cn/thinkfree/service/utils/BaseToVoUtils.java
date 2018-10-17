package cn.thinkfree.service.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * model转换为vo
 *
 * @author gejiaming
 */
public class BaseToVoUtils {

    /**
     * object1转为object2
     *
     * @param object1
     * @param t
     * @param map
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getVo(Object object1, Class<T> t, Map<String, String> map) throws Exception {
        Set<String> set = map.keySet();
        T object2 = t.newInstance();
        for (String string : set) {
            //获取目标对象相应属性的set方法
            Field field = t.getDeclaredField(map.get(string));
            field.setAccessible(true);
            Field field1 = object1.getClass().getDeclaredField(string);
            field1.setAccessible(true);
            field.set(object2,field1.get(object1));
        }
        return object2;
    }

    /**
     * List实体替换
     *
     * @param list
     * @param t
     * @param map
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> getListVo(List list, Class<T> t, Map<String, String> map) throws Exception {
        List<T> newlist = new ArrayList<>();
        for (Object object1 : list) {
            T t1 = getVo(object1, t, map);
            newlist.add(t1);
        }
        return newlist;
    }

    public static Map<String, String> getBigMap() {
        Map<String, String> map = new HashMap<>();
        map.put("", "");
        return map;
    }

    public static Map<String, String> getSmallMap() {
        Map<String, String> map = new HashMap<>();
        map.put("sort", "sort");
        map.put("category", "category");
        map.put("classification", "classification");
        map.put("code", "code");
        map.put("name", "name");
        map.put("unit", "unit");
        map.put("parentSort", "parentSort");
        map.put("technologyIntroduce", "technologyIntroduce");
        map.put("accessories", "accessories");
        map.put("checkStandard", "checkStandard");
        map.put("standardSource", "standardSource");
        map.put("remarks", "remarks");
        map.put("status", "status");
        map.put("createTime", "createTime");
        return map;
    }

}
