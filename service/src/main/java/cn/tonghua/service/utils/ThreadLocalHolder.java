package cn.tonghua.service.utils;

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
