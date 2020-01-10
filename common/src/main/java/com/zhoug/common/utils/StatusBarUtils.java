package com.zhoug.common.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * @Author HK-LJJ
 * @Date 2019/11/4
 * @Description 状态栏工具类
 */
public class StatusBarUtils {
    //transparent 透明
    //translucent 半透明
    private static final String TAG = "StatusBarUtil";
    private static int statusHeight;
    private static final int STATUS_VIEW_ID = 0x56488;


    /**
     * 设置Activity对应的顶部状态栏的颜色,不能为渐变色,
     * api21以上有效
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        }
    }


    /**
     * 隐藏状态栏
     * 状态栏隐藏后,顶部显示黑色,布局不能占用状态栏空间
     *
     * @param activity
     * @param hidden
     */
    public static void hideStatusBar(Activity activity, boolean hidden) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (hidden) {
            attributes.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(attributes);
        } else {
            attributes.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(attributes);
        }

    }

    /**
     * 设置状态栏透明,且布局占用状态栏
     * 支持api19(android 4.4)以上
     *
     * @param activity
     */
    public static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            //状态栏不占位,即布局充满状态栏
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //设置背景透明
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                //api19 设置状态栏半透明
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

        }
    }

    /**
     * 要在activity.setContentView()方法之后调用
     * 设置activity根布局是否给系统ui预留空间
     * 如果设置为true,那么会触发View的padding属性来给系统窗口留出空间,
     * 状态栏显示在padding预留的空间上面.设置状态栏透明后,颜色看起来就是布局的背景色
     */
    public static void setFitsSystemWindows(Activity activity, boolean fitSystemWindows) {
        //获取contentView即setContentView方法所设置的View, 实质为 ContentFrameLayout
        ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView != null) {
            //第一个子View ,即activity布局文件中的根layout
            View rootView = contentView.getChildAt(0);
            if (rootView != null) {
                rootView.setFitsSystemWindows(fitSystemWindows);
            }
        }
    }

    /**
     * 添加状态栏占位组件,默认背景是跟布局背景一样
     *要在activity.setContentView()方法之后调用
     * @param activity
     */
    public static void addStatusPlaceView(Activity activity) {
        //contentView
        ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView != null) {
            //父View:FitWindowsLinearLayout
            ViewGroup parent = (ViewGroup) contentView.getParent();
            //避免重复添加
            View statusView = parent.findViewById(STATUS_VIEW_ID);
            if (statusView != null && statusView.getHeight() == statusHeight) {
                return;
            }
            statusView = createStatusPlaceView(activity);
            //activity的根View
            View rootView = contentView.getChildAt(0);
            Log.d(TAG, "addStatusPlaceView:rootView="+rootView);
            if (rootView != null) {
                Drawable background = rootView.getBackground();
                Log.d(TAG, "addStatusPlaceView:background="+background);
                if (background != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        statusView.setBackground(background);
                    } else {
                        statusView.setBackgroundDrawable(background);
                    }
                }

            }
            parent.addView(statusView, 0);
        }
    }

    /**
     * 添加状态栏占位组件
     *要在activity.setContentView()方法之后调用
     * @param activity
     * @param color
     */
    public static void addStatusPlaceView(Activity activity, @ColorInt int color) {
        //contentView
        ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView != null) {
            //父View:FitWindowsLinearLayout
            ViewGroup parent = (ViewGroup) contentView.getParent();
            //避免重复添加
            View statusView = parent.findViewById(STATUS_VIEW_ID);
            if (statusView != null && statusView.getHeight() == statusHeight) {
                return;
            }
            statusView = createStatusPlaceView(activity);
            statusView.setBackgroundColor(color);
            parent.addView(statusView, 0);
        }
    }

    /**
     * 添加状态栏占位组件
     *要在activity.setContentView()方法之后调用
     * @param activity
     * @param drawable
     */
    public static void addStatusPlaceView(Activity activity, Drawable drawable) {
        //contentView
        ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView != null) {
            //父View:FitWindowsLinearLayout
            ViewGroup parent = (ViewGroup) contentView.getParent();
            //避免重复添加
            View statusView = parent.findViewById(STATUS_VIEW_ID);
            if (statusView != null && statusView.getHeight() == statusHeight) {
                return;
            }
            statusView = createStatusPlaceView(activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                statusView.setBackground(drawable);
            } else {
                statusView.setBackgroundDrawable(drawable);
            }
            parent.addView(statusView, 0);
        }
    }

    /**
     * 添加状态栏占位组件
     *要在activity.setContentView()方法之后调用
     * @param activity
     * @param resId
     */
    public static void addStatusPlaceView1(Activity activity, @DrawableRes int resId) {
        //contentView
        ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView != null) {
            //父View:FitWindowsLinearLayout
            ViewGroup parent = (ViewGroup) contentView.getParent();
            //避免重复添加
            View statusView = parent.findViewById(STATUS_VIEW_ID);
            if (statusView != null && statusView.getHeight() == statusHeight) {
                return;
            }
            statusView = createStatusPlaceView(activity);
            statusView.setBackgroundResource(resId);
            parent.addView(statusView, 0);
        }
    }


    /**
     * 移除状态栏占位组件
     *
     * @param activity
     */
    public static void removeStatusPlaceView(Activity activity) {
        ViewGroup contentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView != null) {
            //父View:FitWindowsLinearLayout
            ViewGroup parent = (ViewGroup) contentView.getParent();

            View statusView = parent.findViewById(STATUS_VIEW_ID);
            if (statusView != null && statusView.getHeight() == statusHeight) {
                parent.removeView(statusView);
            }
        }
    }

    /**
     * 沉侵式,默认添加状态栏占位View
     *要在activity.setContentView()方法之后调用
     * @param activity
     */
    public static void setImmersive(Activity activity, boolean addStatusPlaceView) {
        //状态栏透明,且布局占用状态栏
        transparentStatusBar(activity);
        //不给状态栏预留位置
        setFitsSystemWindows(activity, false);
        if (addStatusPlaceView) {
            //添加状态栏占位
            addStatusPlaceView(activity);
        }

    }


    /**
     * 创建新的状态栏占位组件
     *
     * @param activity
     * @return
     */
    private static View createStatusPlaceView(Activity activity) {
        View statusView = new View(activity);
        statusView.setId(STATUS_VIEW_ID);
        if (statusHeight <= 0) {
            statusHeight = AppUtils.getStatusHeight(activity);
            Log.d(TAG, "createStatusPlaceView:statusHeight=" + statusHeight);
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusHeight);
        statusView.setLayoutParams(params);
        return statusView;
    }

    /**
     * 隐藏/显示导航栏
     * @param activity
     * @param hidden
     */
    public static void hideNavigationBar(Activity activity, boolean hidden) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = activity.getWindow().getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            if (hidden) {
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(systemUiVisibility);
            } else {
                systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION &
                        ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION &
                        ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(systemUiVisibility);
            }
        }
    }



    public static void sss(Activity activity,boolean hidden){
        //全屏视频模式
        View decorView = activity.getWindow().getDecorView();
        if (hidden){
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

    }


}
