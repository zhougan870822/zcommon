package com.zhoug.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast 工具 可以在主线程和子线程中使用
 * 支持改变文字颜色和背景颜色
 *
 * @Author HK-LJJ
 * @Date 2019/10/14
 * @Description TODO
 */
public class ToastUtils {

    public static void toastShort(Context context, Object msg) {
        Toast.makeText(context, msg == null ? "null" : msg.toString(), Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, Object msg) {
        Toast.makeText(context, msg == null ? "null" : msg.toString(), Toast.LENGTH_LONG).show();
    }

    public static void toastShortCenter(Context context, Object msg) {
        Toast toast = Toast.makeText(context, msg == null ? "null" : msg.toString(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toastLongCenter(Context context, Object msg) {
        Toast toast = Toast.makeText(context, msg == null ? "null" : msg.toString(), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
