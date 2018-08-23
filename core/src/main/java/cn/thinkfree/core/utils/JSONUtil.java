package cn.thinkfree.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;

/**
 * JSON 工具类
 * 提供对象与String 之间转换
 */
public class JSONUtil {
    private static Gson gson;

    private static Gson getGson(){
        if(gson == null){
            gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
        }
        return gson;
    }

    /**
     * Json格式转对象
     * @param jsonStr   Json字符串
     * @param targetCls 对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonStr,Class<T> targetCls){
        T targetObj=getGson().fromJson(jsonStr, TypeToken.get(targetCls).getType());
        return targetObj;
    }

    /**
     *  根据数据流转换成对象
     * @param reader
     * @param targetCls
     * @param <T>
     * @return
     */
    public static <T> T reader2Bean(Reader reader,Class<T> targetCls){
        T targetObj=getGson().fromJson(reader, TypeToken.get(targetCls).getType());
        return targetObj;
    }

    /**
     * 对象转Json串
     * @param obj 对象
     * @return
     */
    public static String bean2JsonStr(Object obj){
        return getGson().toJson(obj);
    }
    /**
     * 对象解析方法
     * @param obj 源数据
     * @param kls 要解析成的对象
     * @param <T>
     * @return
     */
    public  static <T> T  beanParser(Object obj,Class kls) {

        return getGson().fromJson(getGson().toJson(obj), TypeToken.get(kls).getType());
    }

}
