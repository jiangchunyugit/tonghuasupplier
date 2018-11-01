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
     * object1转为object2(属性名相同)
     *
     * @param object1
     * @param t
     * @param map
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getVo(Object object1, Class<T> t, Map<String, String> map)  {
        T object2 = null;
        try {
            Set<String> set = map.keySet();
            object2 = t.newInstance();
            for (String string : set) {
                //获取目标对象相应属性的set方法
                Field field = t.getDeclaredField(map.get(string));
                field.setAccessible(true);
                Field field1 = object1.getClass().getDeclaredField(string);
                field1.setAccessible(true);
                field.set(object2,field1.get(object1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object2;
    }

    /**
     * object1转为object2(属性名不同)
     * @param object1
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getVo(Object object1, Class<T> t)  {
        T object2 = null;
        try {
            object2 = t.newInstance();
            Field[] declaredFields = t.getDeclaredFields();
            for (int i= 0;i<declaredFields.length;i++){
                Field field = declaredFields[i];
                field.setAccessible(true);
                Field field1 = null;
                try {
                    field1 = object1.getClass().getDeclaredField(field.getName());
                } catch (NoSuchFieldException e) {
                    System.out.println("字段未匹配:"+field.getName());
                    continue;
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                field1.setAccessible(true);
                field.set(object2,field1.get(object1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object2;
    }

    /**
     * List实体替换(两个实体属性名不同)
     *
     * @param list
     * @param t
     * @param map
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> getListVo(List list, Class<T> t, Map<String, String> map) {
        List<T> newlist = new ArrayList<>();
        for (Object object1 : list) {
            T t1 = getVo(object1, t, map);
            newlist.add(t1);
        }
        return newlist;
    }


    /**
     * List实体替换(两个实体属性名相同)
     * @param list
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> getListVo(List list, Class<T> t) {
        List<T> newlist = new ArrayList<>();
        for (Object object1 : list) {
            T t1 = getVo(object1, t);
            newlist.add(t1);
        }
        return newlist;
    }

    /**
     * map.key     为model的属性
     * map.value   为VO的属性
     */
    public static Map<String, String> getDataDetaiMap() {
        Map<String, String> map = new HashMap<>();
        map.put("", "uploadTime");
        map.put("", "type");
        map.put("", "confirm");
        map.put("", "thirdUrl");
        return map;
    }

    /**
     * map.key     为model的属性
     * map.value   为VO的属性
     */
    public static Map<String, String> getProjectMap() {
        Map<String, String> map = new HashMap<>();
        map.put("projectNo", "projectNo");
        map.put("stage", "stage");
        map.put("address", "address");
        map.put("releaseTime", "releaseTime");
        map.put("imgUrl", "imgUrl");
        map.put("thirdView", "thirdView");
        return map;
    }

    public static Map<String, String> getBaseSmallMap() {
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
