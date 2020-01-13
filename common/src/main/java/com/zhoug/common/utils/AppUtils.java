package com.zhoug.common.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Surface;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.zhoug.common.Constant;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * App工具 哈哈我也不晓得怎么命名
 */
public class AppUtils {
    private static final String TAG = "AppUtils";

    private static Application application;
    /**
     * 取得屏幕宽高,不包括系统装饰(eg:状态栏,导航栏)
     *
     * @param context
     * @return
     */
    public static Point getScreenSize(Context context) {
        Point size = new Point();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            size.x = outMetrics.widthPixels;//宽
            size.y = outMetrics.heightPixels;//高
            //或者
//            manager.getDefaultDisplay().getSize(size);
        }

        return size;
    }

    /**
     * 取得屏幕宽高(屏幕的真实宽高,包括系统装饰eg:状态栏,导航栏)
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Point getRealScreenSize(Context context) {
        Point size = new Point();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getRealMetrics(outMetrics);
            size.x = outMetrics.widthPixels;//宽
            size.y = outMetrics.heightPixels;//高
        }

        return size;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取底部导航栏的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 底部导航栏是否显示
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isNavigationBarShow(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            DisplayMetrics out = new DisplayMetrics();
            DisplayMetrics realOut = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(out);
            manager.getDefaultDisplay().getRealMetrics(realOut);

            int height=0;
            int realHeight=0;
            int statusHeight = getStatusHeight(context);
            int windowRotation = getWindowRotation(context);
            //竖屏
            if(windowRotation==0 || windowRotation==180){
                height=out.heightPixels;
                realHeight=realOut.heightPixels;
            }else{
                height=out.widthPixels;
                realHeight=realOut.widthPixels;
            }
            if(realHeight-height-statusHeight>0){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取窗口旋转角度(0/90/180/270)
     *
     * @return
     */
    public static int getWindowRotation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        //手机旋转的角度
        int  windowRotation  = 0;
        switch (rotation) {
            case Surface.ROTATION_0://竖屏
                windowRotation = 0;
                break;
            case Surface.ROTATION_90://逆时针旋转90度(左横屏)
                windowRotation = 90;
                break;
            case Surface.ROTATION_180://逆时针旋转180度(倒竖屏)
                windowRotation = 180;
                break;
            case Surface.ROTATION_270://逆时针旋转270度(右横屏)
                windowRotation = 270;
                break;
        }
        return windowRotation;
    }

    /**
     * 是否是竖屏
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context){
        int orientation = context.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            return true;
        }
        return false;
    }


    /**
     * 获取设备序列号
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String deviceId = "000";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId=telephonyManager.getImei();
                }else{
                    deviceId=telephonyManager.getDeviceId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceId;
    }

    /**
     * 获取本机号码
     * @param context
     * @return
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getLocalPhoneNumber(Context context){
        String phone = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                phone= telephonyManager.getLine1Number();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return phone;
    }


    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取版本名字
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断app是否在运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processesInErrorState = am.getRunningAppProcesses();
        if (processesInErrorState != null && processesInErrorState.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo info : processesInErrorState) {
                if (info.processName.equals(packageName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断应用是否在前台运行
     *
     * @param context
     * @return 在前台返回true(包括在前台是息屏)
     */
    public static boolean isAppInFront(Context context) {
        boolean isFront = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses != null && runningAppProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
                //前台的应用
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (processInfo.pkgList != null) {
                        for (String pkg : processInfo.pkgList) {
                            if (pkg.equals(context.getPackageName())) {
                                isFront = true;
                            }
                        }
                    }
                }
            }
        }
        return isFront;
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dipTopx(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int pxTosp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int spTopx(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 获取自定义属性的值 int
     *
     * @param theme
     * @param id
     * @return
     */
    public static int getAttrValueIn(Resources.Theme theme, @AttrRes int id) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(id, typedValue, true);
        return typedValue.data;
    }

    /**
     * 获取自定义属性的值 String
     *
     * @param theme
     * @param id
     * @return
     */
    public static String getAttrValueString(Resources.Theme theme, @AttrRes int id) {
        TypedValue typedValue1 = new TypedValue();
        theme.resolveAttribute(id, typedValue1, true);
        return typedValue1.toString();
    }

    /**
     * 测量组件的大小
     *
     * @param view
     */
    public static void measureView(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int HeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, HeightMeasureSpec);
    }



    /**
     * 安装apk文件
     * 8.0需要在manifest.xml 中注册权限REQUEST_INSTALL_PACKAGES
     */
    public static void installApk(Context context, File file, String authority) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("apk文件不存在" + file.getAbsolutePath());
        } else {
            Uri contentUri = null;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//授予临时权限别忘了
                contentUri = FileProvider.getUriForFile(context, authority, file);
                Log.i(TAG, "getFileProvideIntent: " + contentUri);
            } else {
                contentUri = Uri.fromFile(file);
            }
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
            Log.i(TAG, "installApk: 安装apk");


        }
    }



    /**
     * 检测程序是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }





    /**
     * 系统所认为的最小滑动距离TouchSlop
     *
     * @param context
     * @return
     */
    public static int getTouchSlop(Context context) {
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 拍摄照片和视频后相册中没有文件，的解决办法
     * @param context
     * @param path
     */
    public static void scannerFile(Context context,String path){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 隐藏键盘
     */
    public static void hideSoftKeyboard(Activity activity) {
        /**
         * 另外，避免软键盘弹出会覆盖底部控件的方法是在布局文件根布局加上一个属性：
         * android:fitsSystemWindows="true"
         */
        if (activity == null) return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null == imm || !imm.isActive()) return;
        View currentFocus = activity.getCurrentFocus();

        if (currentFocus != null) {
            //有焦点关闭
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            //无焦点关闭
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }

    }


    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @return 应用程序是/否获取Root权限
     */
    public static boolean requestRootPermission(Context context) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd="chmod 777 " + context.getPackageCodePath();
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if(process!=null){
                    process.destroy();
                }
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     * 使用反射获取Application实例
     * @return {@link #application}
     */
    public static Application getApplicationByReflect() {
        if(application==null){
            synchronized (AppUtils.class){
                if(application==null){
                    try {
                        @SuppressLint("PrivateApi")
                        Class<?> activityThread = Class.forName("android.app.ActivityThread");
                        Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
                        Object app = activityThread.getMethod("getApplication").invoke(thread);
                        if (app == null) {
                            throw new NullPointerException("u should init first");
                        }
                        application=(Application) app;
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return application;
    }
}
