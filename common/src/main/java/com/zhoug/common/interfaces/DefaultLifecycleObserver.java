package com.zhoug.common.interfaces;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

import com.zhoug.common.utils.LogUtils;


/**
 *
 *生命周期观察者,生命周期变化后自动回掉对应的方法,默认实现
 *
 * @Author HK-LJJ
 * @Date 2019/11/27
 * @Description
 */
public interface DefaultLifecycleObserver extends FullLifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    default void onCreate(@NonNull LifecycleOwner owner) {
//        LogUtils.d(">>>LifecycleObserver", "onCreate:");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    default void onStart(@NonNull LifecycleOwner owner) {
//        LogUtils.d(">>>LifecycleObserver", "onStart:");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    default void onResume(@NonNull LifecycleOwner owner) {
//        LogUtils.d(">>>LifecycleObserver", "onResume:");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    default void onPause(@NonNull LifecycleOwner owner) {
//        LogUtils.d(">>>LifecycleObserver", "onPause:");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    default void onStop(@NonNull LifecycleOwner owner) {
//        LogUtils.d(">>>LifecycleObserver", "onStop:");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    default void onDestroy(@NonNull LifecycleOwner owner) {
        LogUtils.d(">>>LifecycleObserver", "onDestroy:");

    }


}
