package com.zhoug.common.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

/**
 * 描述：资源工具
 * zhougan
 * 2019/3/24
 **/
public class ResourceUtils {
    /**
     * 获取资源文件的id
     *
     * @param context
     * @param resName eg: android:id="@+id/btn1"中的btn1
     * @return
     */
    public static int getId(Context context, String resName) {
        //getIdentifier参数:资源的名称,资源的类型(drawable,string等),包名
        return context.getResources().getIdentifier(resName, "id", context.getPackageName());
    }

    /**
     * 获取资源文件中string的id
     *
     * @param context
     * @param resName eg: <string name="app_name">AndroidCommon</string>中的app_name
     * @return
     */
    public static int getStringId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "string", context.getPackageName());
    }


    /**
     * 获取资源文件drawable的id
     *
     * @param context
     * @param resName eg:ic_launcher_background   /drawable/ic_launcher_background.xml
     * @return
     */
    public static int getDrawableId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }


    /**
     * 获取资源文件layout的id
     *
     * @param context
     * @param resName eg: activity_main    /layout/activity_main.xml
     * @return
     */
    public static int getLayoutId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "layout", context.getPackageName());
    }


    /**
     * 获取资源文件style的id
     *
     * @param context
     * @param resName eg:AppTheme  <style name="AppTheme"></style>
     * @return
     */
    public static int getStyleId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "style", context.getPackageName());
    }

    /**
     * 获取资源文件color的id
     *
     * @param context
     * @param resName eg:colorPrimary    <color name="colorPrimary">#008577</color>
     * @return
     */
    public static int getColorId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "color", context.getPackageName());
    }

    /**
     * 获取资源文件dimen的id
     *
     * @param context
     * @param resName eg:zxc     <dimen name="zxc">12dp</dimen>
     * @return
     */
    public static int getDimenId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "dimen", context.getPackageName());
    }

    /**
     * 获取资源文件ainm的id
     *
     * @param context
     * @param resName eg:test  /anim/test.xml
     * @return
     */
    public static int getAnimId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "anim", context.getPackageName());
    }

    /**
     * 获取资源文件menu的id
     *
     * @param context
     * @param resName eg:menu1   /menu/menu1.xml
     * @return
     */
    public static int getMenuId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "menu", context.getPackageName());
    }

    /**
     * 获取资源文件xml的id
     *
     * @param context
     * @param resName eg:file_paths   /xml/file_paths.xml
     * @return
     */
    public static int getXmlId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "xml", context.getPackageName());
    }

    /**
     * 获取颜色 {@link ContextCompat}
     * @param context
     * @param id
     * @return
     */
    public static int getColor(Context context, @ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }


}
