package com.zhoug.common.base;

import android.app.Activity;

import java.util.Stack;

/**
 * 管理所有activity 单例模式
 * @Author HK-LJJ
 * @Date 2019/12/5
 * @Description
 */
public class ActivityManager {
    private static final String TAG = ">>>ActivityManager";
    private Stack<Activity> activityStack;//activity集合
    private static ActivityManager instance;

    private ActivityManager() {

    }

    /**
     * 获取单例
     * @return
     */
    public static ActivityManager get(){
        if(instance==null){
            synchronized (ActivityManager.class){
                if(instance==null){
                    instance=new ActivityManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈,
     * 最好在BaseActivity的OnCreate方法调用
     * 获取application.registerActivityLifecycleCallbacks()中相应的方法中调用
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (activity != null) {
            activityStack.push(activity);
//            LogUtils.d(TAG, "addActivity:" + activity);
        }
    }

    /**
     * 移除指定的Activity,
     * 最好在BaseActivity的onDestroy方法调用
     */
    public void removeActivity(Activity activity) {
        if (activityStack != null && activity != null) {
            if (activityStack.remove(activity)) {
//                LogUtils.d(TAG, "removeActivity:" + activity);
            }

        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack != null)
            return activityStack.lastElement();

        return null;
    }

    /**
     * 获取指定的Activity
     * @author kymjs
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity != null && activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 结束指定的Activity
     */
    protected void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
//                LogUtils.d(TAG, "finishActivity:" + activity);
            }
        }
    }

    /**
     * 结束指定类名的Activity并移出栈
     */
    public void finishActivity(Class<?> cls) {
        finishActivity(getActivity(cls));
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）并移出栈
     */
    public void finishActivity() {
        finishActivity(currentActivity());
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null || activityStack.empty()) {
            return;
        }
        while (!activityStack.empty()) {
            //移出栈顶activity
            Activity pop = activityStack.pop();
            finishActivity(pop);
        }
        activityStack.clear();
    }


}
