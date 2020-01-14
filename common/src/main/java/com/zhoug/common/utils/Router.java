package com.zhoug.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * 页面跳转路由
 */
public class Router {
    private static final String TAG = ">>>Router";

    /**
     * 找到或者创建新的Fragment
     *
     * @param cls
     * @param fm
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Fragment> T findOrCreateByTag(Class<T> cls, FragmentManager fm) {
        T fragmentByTag = (T) fm.findFragmentByTag(cls.getName());
        if (fragmentByTag == null) {
            try {
                fragmentByTag = cls.newInstance();
                LogUtils.d(TAG, "findOrCreateByTag: 创建新的" + cls.getName());
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return fragmentByTag;
    }

    public static void navigation(Context context, Class<? extends Activity> target) {
        navigation(context, target, null);
    }

    public static void navigation(Context context, Class<? extends Activity> target, Bundle args) {
        Intent intent = new Intent(context, target);
        if (args != null) {
            intent.putExtras(args);
        }
        context.startActivity(intent);
    }

    public static void navigationForResult(Activity activity, Class<? extends Activity> target, Bundle args, int requestCode) {
        Intent intent = new Intent(activity, target);
        if (args != null) {
            intent.putExtras(args);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void navigationForResult(Fragment fragment, Class<? extends Activity> target, Bundle args, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), target);
        if (args != null) {
            intent.putExtras(args);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转动画,需要在开始跳转或者关闭页面时调用
     * @param activity
     * @param enterAnim
     * @param exitAnim
     */
    public static void transitionAnim(Activity activity,int enterAnim, int exitAnim){
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

}
