package com.zhoug.common.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 *
 * @Author HK-LJJ
 * @Date 2019/12/5
 * @Description
 */
public abstract class AbsApplication extends Application {

    @Override

    public void onCreate() {
        super.onCreate();
        //注册activity的生命周期回掉
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    /**
     * activity生命周期回掉
     */
    protected ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ActivityManager.get().addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ActivityManager.get().removeActivity(activity);
        }
    };


}
