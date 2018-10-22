package cn.thinkfree.core.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/22 17:25
 */
public class ThreadManager {
    /**
     * 通过ThreadPoolExecutor的代理类来对线程池的管理
     */
    private static ThreadPollProxy mThreadPollProxy;
    /**
     * 单例对象
     */
    public static ThreadPollProxy getThreadPollProxy(){
        synchronized (ThreadPollProxy.class) {
            if(mThreadPollProxy==null){
                mThreadPollProxy=new ThreadPollProxy(3,6,1000);
            }
        }
        return mThreadPollProxy;
    }
    /**
     * 通过ThreadPoolExecutor的代理类来对线程池的管理
     */
    public static class ThreadPollProxy{

        private static final int CAPACITY = 1024;

        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private ThreadPoolExecutor poolExecutor;


        public ThreadPollProxy(int corePoolSize,int maximumPoolSize,long keepAliveTime){
            this.corePoolSize=corePoolSize;
            this.maximumPoolSize=maximumPoolSize;
            this.keepAliveTime=keepAliveTime;
        }
        /**
         * 对外提供一个执行任务的方法
         */
        public void execute(Runnable r){
            if(poolExecutor == null || poolExecutor.isShutdown()){

                ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                        .setNameFormat("thread-pool-%d").build();

                poolExecutor = new ThreadPoolExecutor(
                        corePoolSize,
                        maximumPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(CAPACITY),
                        namedThreadFactory,
                        new ThreadPoolExecutor.AbortPolicy());
            }
            poolExecutor.execute(r);
        }
    }
}
