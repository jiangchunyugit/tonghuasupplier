package cn.thinkfree.service.utils;

import cn.thinkfree.database.model.UserRegister;

public class ThreadLocalHolder {
    private static ThreadLocal<Object> threadLocal = new ThreadLocal(){
        @Override
        protected Object initialValue() {
            return null;
        }
    };

    public static void set(Object obj){

        threadLocal.set(obj);

    }

    public static Object get(){
        return threadLocal.get();
    }

    public static void clear(){
        threadLocal.remove();
    }
}
