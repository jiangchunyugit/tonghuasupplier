package cn.thinkfree.core.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;


public class Util
{
    /**
     * 
     * 校验2个对象是否相同
     * <p>
     * 校验时不区分具体的类型，如果其中一个参数为空，则总是返回false 对于collection对象会校验列表中每个对象
     * 
     * @param first
     *            待比较的参数1
     * @param second
     *            待比较的参数2
     * @return 参数相等时返回true，如果有一个为空，则返回false，对于collection对象会校验列表中每个对象是否相等
     */
    public static boolean equal(final Object first, final Object second) {
        if (first == null || second == null) {
            return false;
        }
        if (first == second) {
            return true;
        }
        if (first.equals(second)) {
            return true;
        }
        if (first instanceof Number && second instanceof Number) {
            return first.toString().equals(second.toString());
        }
        if (first instanceof Collection<?> && second instanceof Collection<?>) {
            Collection<?> cA = (Collection<?>) first;
            Collection<?> cB = (Collection<?>) second;
            return equal(cA, cB);
        }
        if (first instanceof Map<?, ?> && second instanceof Map<?, ?>) {
            Map<?, ?> mA = (Map<?, ?>) first;
            Map<?, ?> mB = (Map<?, ?>) second;
            return equal(mA, mB);
        }
        if (first.getClass().isArray() && second.getClass().isArray()) {
            Object[] firstArray = (Object[]) first;
            Object[] secondArray = (Object[]) second;
            return equal(firstArray, secondArray);
        }
        return false;
    }

    /**
     * 
     * 判断2个字符串是否相同
     * 
     * @param first
     *            待比较的字符串1
     * @param second
     *            待比较的字符串2
     * @return 如果有一个为空，则返回false,之后进行equals比较相等返回true
     */
    public static boolean equal(final String first, final String second) {
        if (first == null || second == null) {
            return false;
        }
        return first.equals(second);
    }

    /**
     * 
     * 判断2个map是否相同
     * <p>
     * 如果有一个为空，则返回false, 之后比较长度以及每个元素是否相同，不比较map本身的类型是否相同
     * 
     * @param first
     *            待比较的参数1
     * @param second
     *            待比较的参数2
     * @return 如果有一个为空，则返回false,之后比较长度以及每个元素是否相同
     */
    public static boolean equal(final Map<?, ?> first, final Map<?, ?> second) {
        if (first == null || second == null) {
            return false;
        }
        if (first == second) {
            return true;
        }
        if (first.size() != second.size()) {
            return false;
        }
        Iterator<?> entryIt = first.entrySet().iterator();
        while (entryIt.hasNext()) {
            Entry<?, ?> entry = (Entry<?, ?>) entryIt.next();
            if (entry.getValue() == null) {
                return false;
            }
            if (!second.containsKey(entry.getKey())) {
                return false;
            }
            if (!equal(entry.getValue(), second.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * 比较2个array是否相同
     * <p>
     * 按顺序比较其内部每个对象是否相同
     * 
     * @param first
     *            待比较的参数1
     * @param second
     *            待比较的参数2
     * @return 如果有一个为空，则返回false,之后按顺序比较其内部每个对象是否相同
     */
    public static boolean equal(final Object[] first, final Object[] second) {
        if (first == null || second == null) {
            return false;
        }
        if (first == second) {
            return true;
        }
        if (first.length != second.length) {
            return false;
        }
        int len = first.length;
        for (int i = 0; i < len; i++) {
            if (!equal(first[i], second[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否为null
     * 
     * @param obj
     *            待判断的对象
     * @return obj==null返回true
     */
    public static boolean isNull(final Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }

    /**
     * 判断对象是否为空
     * <p>
     * 对象为null则返回空， 对于字符串会判断是否为空串，只有tab键或者多个空格都认为是空， 另外对于array判断长的是否为0
     * 对于collection、map则调用其isEmpty方法进行判断
     * 
     * @param obj
     *            被判定的对象
     * @return 为null则返回true，如果为空串或只有tab键或者多个空格都认为是空，对于array判断长的是否为0，返回true
     */
    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return isEmpty((String) obj);
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map<?, ?>) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        return false;
    }

    /**
     * 
     * 判断Collection是否为空或者null
     * 
     * @param obj
     *            被判定的对象
     * @return null返回true，否则调用isEmpty()方法
     */
    public static boolean isEmpty(final Collection<?> obj) {
        if (obj == null) {
            return true;
        }
        return obj.isEmpty();
    }

    /**
     * 
     * 判断Map是否为空或者null
     * 
     * @param obj
     *            被判定的对象
     * @return null返回true，否则调用isEmpty()方法
     */
    public static boolean isEmpty(final Map<?, ?> obj) {
        if (obj == null) {
            return true;
        }
        return obj.isEmpty();
    }

    /**
     * 
     * 判断数组是否为空或者null
     * 
     * @param obj
     *            被判定的对象
     * @return length==0，null均返回true
     */
    public static boolean isEmpty(final Object[] obj) {
        if (obj == null) {
            return true;
        }
        if (obj.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 
     * 判断字符串是否为空或者null
     * 
     * @param obj
     * @return 回车、tab键或者多个空格，null均返回true
     */
    public static boolean isEmpty(final String obj) {
        if (obj == null) {
            return true;
        }
        if (equal("", obj.trim())) {
            return true;
        }
        return false;
    }


    /**
     * 较方便的创建一个数组，比如：
     * 
     * <pre>
     * Pet[] pets = Util.array(pet1, pet2, pet3);
     * </pre>
     * 
     * @param eles
     *            可变参数
     * @return 数组对象
     */
    public static <T> T[] array(final T... eles) {
        return eles;
    }

    /**
     * 较方便的创建一个列表，比如：
     * 
     * <pre>
     * List《Pet》 pets = Util.list(pet1, pet2, pet3);
     * </pre>
     * 
     * 注，这里的 List，是 ArrayList 的实例
     * 
     * @param eles
     *            可变参数
     * @return 列表对象
     */
    public static <T> List<T> list(final T... eles) {
        List<T> list = new ArrayList<T>(eles.length);
        for (T ele : eles) {
            list.add(ele);
        }
        return list;
    }

    /**
     * 
     * 较方便的创建一个map
     * <p>
     * 注，这里的 Map，是 HashMap 的实例
     * 
     * @param <K>
     *            key的类型
     * @param <V>
     *            value的类型
     * @return map对象
     */
    public static <K, V> Map<K, V> map() {
        return new HashMap<K, V>();
    }

    /**
     * 
     * 较方便的创建一个map
     * <p>
     * 注，这里的 Map，是 HashMap 的实例
     * 
     * @param <K>
     *            key的类型
     * @param <V>
     *            value的类型
     * @param key
     *            键
     * @param value
     *            值
     * @return map对象，包含key、value
     */
    public static <K, V> Map<K, V> map(final K key, final V value) {
        Map<K, V> map = map();
        map.put(key, value);
        return map;
    }


    /**
     * 
     * 较方便的创建一个map
     * <p>
     * 注，这里的 Map，是 LinkedHashMap 的实例
     * 
     * @param <K>
     *            key的类型
     * @param <V>
     *            value的类型
     * @return map对象
     */
    public static <K, V> Map<K, V> linkedMap() {
        return new LinkedHashMap<K, V>();
    }

    /**
     * 
     * 较方便的创建一个map
     * <p>
     * 注，这里的 Map，是 LinkedHashMap 的实例
     * 
     * @param <K>
     *            key的类型
     * @param <V>
     *            value的类型
     * @param key
     *            键
     * @param value
     *            值
     * @return map对象，包含key、value
     */
    public static <K, V> Map<K, V> linkedMap(final K key, final V value) {
        Map<K, V> map = linkedMap();
        map.put(key, value);
        return map;
    }


    /**
     * 将集合变成指定类型的数组
     * 
     * @param collection
     *            集合对象
     * @param eleType
     *            数组元素类型
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] collection2array(final Collection<?> collection,
            final Class<E> eleType) {
        if (null == collection) {
            return (E[]) Array.newInstance(eleType, 0);
        }
        Object re = Array.newInstance(eleType, collection.size());
        int i = 0;
        for (Object obj : collection) {
            if (null == obj) {
                Array.set(re, i++, null);
            } else {
                Array.set(re, i++, eleType.cast(obj));
            }
        }
        return (E[]) re;
    }

    /**
     * 将集合变成 ArrayList
     * 
     * @param col
     *            集合对象
     * @return 列表对象
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> collection2list(final Collection<E> col) {
        if (null == col) {
            return new ArrayList<E>(0);
        }
        if (col.size() == 0) {
            return new ArrayList<E>(0);
        }
        Class<E> eleType = (Class<E>) col.iterator().next().getClass();
        return collection2list(col, eleType);
    }

    /**
     * 将集合编程变成指定类型的列表
     * <p>
     * 根据类型强制转换
     * 
     * @param col
     *            集合对象
     * @param eleType
     *            列表类型
     * @return 列表对象
     */
    public static <E> List<E> collection2list(final Collection<?> col,
            final Class<E> eleType) {
        if (null == col) {
            return new ArrayList<E>(0);
        }
        List<E> list = new ArrayList<E>(col.size());
        for (Object obj : col) {
            list.add(eleType.cast(obj));
        }
        return list;
    }

    /**
     * 获取对象的长度
     * <p>
     * 空或empty返回0 String返回字符串的长度,未进行trim 数组返回数组的长度 Collection返回自身的长度 map返回自身的长度
     * 其他返回obj.toString()的长度
     * 
     * @param obj
     *            待测长度的对象
     * @return 对象的长度
     */
    public static int size(final Object obj) {
        if (isNull(obj)) {
            return 0;
        }
        if (obj instanceof String) {
            return size((String) obj);
        }
        if (obj.getClass().isArray()) {
            return size((Object[]) obj);
        }
        if (obj instanceof Collection<?>) {
            return size((Collection<?>) obj);
        }
        if (obj instanceof Map<?, ?>) {
            return size((Map<?, ?>) obj);
        }
        return obj.toString().length();
    }

    /**
     * 获取对象的长度
     * <p>
     * 空返回0 其他返回字符串的长度
     * 
     * @param obj
     *            待测长度的对象
     * @return 对象的长度
     */
    public static int size(final String obj) {
        if (isEmpty(obj)) {
            return 0;
        }
        return obj.length();
    }

    /**
     * 获取对象的长度
     * <p>
     * 空返回0 其他返回数组的长度
     * 
     * @param obj
     *            待测长度的对象
     * @return 对象的长度
     */
    public static int size(final Object[] obj) {
        if (isEmpty(obj)) {
            return 0;
        }
        return obj.length;
    }

    /**
     * 获取对象的长度
     * <p>
     * 空返回0 其他返回Collection的长度
     * 
     * @param obj
     *            待测长度的对象
     * @return 对象的长度
     */
    public static int size(final Collection<?> obj) {
        if (isEmpty(obj)) {
            return 0;
        }
        return obj.size();
    }

    /**
     * 获取对象的长度
     * <p>
     * 空返回0 其他返回自身的长度
     * 
     * @param obj
     *            待测长度的对象
     * @return 对象的长度
     */
    public static int size(final Map<?, ?> obj) {
        if (isEmpty(obj)) {
            return 0;
        }
        return obj.size();
    }


      /**
      * 将 List 中的值倒入到一个字符数组中;
      */
     public final static String[] listToArray(List<String> list) {
         
         int size = Util.isEmpty(list)?0:list.size();
         String[] array = new String[size];
    
         for (int i = 0; i < size; i++) {
             array[i] = (String) list.get(i);
         }
    
         return array;
     }
     
     public static boolean isNotEmpty(String... values)
     {
         boolean result = true;
         if (values == null || values.length == 0)
         {
             result = false;
         } else
         {
             for (String value : values)
             {
                 result &= !Util.isEmpty(value);
             }
         }
         return result;
     }
}
