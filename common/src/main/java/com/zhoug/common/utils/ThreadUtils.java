package com.zhoug.common.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 线程工具
 *
 * @Author HK-LJJ
 * @Date 2019/10/14
 * @Description TODO
 */
public class ThreadUtils {
    private static final String TAG = ">>>ThreadUtils";
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());


    private ThreadUtils() {

    }


    /**
     * 是否为主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread()
                || Looper.myLooper() == Looper.getMainLooper()
                || "main".equals(Thread.currentThread().getName());
    }

    /**
     * 在主线程中执行任务
     *
     * @param runnable
     */
    public static void runMainThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }

}
