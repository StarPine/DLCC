package com.dl.lib.util;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: 彭石林
 * Time: 2022/9/26 17:43
 * Description: This is ThreadManager
 */
public class MPThreadManager {

    private static final String TAG = MPThreadManager.class.getName();

    /**
     * 线程池的参数采用AsyncTask的配置 -- android 26
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final BlockingQueue<Runnable> POOL_WORK_QUEUE = new LinkedBlockingQueue<>(128);

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "MPThreadManager #" + mCount.getAndIncrement());
        }
    };

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                POOL_WORK_QUEUE, THREAD_FACTORY);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }

    public static class CachedThreadPool{
        /**
         * 从线程池中创建子线程执行异步任务
         * 在任务数超过128，或者线程池Shutdown时将跳过这条任务
         * @param runnable Runnable
         */
        public static void runCachedThreadPool(Runnable runnable) {

            if (THREAD_POOL_EXECUTOR.getQueue().size() == 128 || THREAD_POOL_EXECUTOR.isShutdown()) {
                Log.e(TAG, "线程池爆满警告，请查看是否开启了过多的耗时线程");
                return;
            }
            THREAD_POOL_EXECUTOR.execute(runnable);
        }
    }
}
