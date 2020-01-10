package com.zhoug.common.utils;

import android.util.Log;

/**
 * 日志打印工具
 */
public class LogUtils {
    private static final String TAG = ">>>LogUtils";
    public static final int RELEASE=1;
    public static final int DEBUG=5;
    private static int status=RELEASE;

    /**
     * 是否开启debug模式
     * @param debug
     */
    public static void setDebug(boolean debug ){
        if(debug){
            status=DEBUG;
        }else {
            status=RELEASE;
        }
    }

    public static void i(String TAG,String msg){
        if(status>=RELEASE){
            Log.i(TAG, msg);
        }
    }

    public static void d(String TAG,String msg){
        if(status>=DEBUG){
            Log.d(TAG, msg);
        }
    }

    public static void e(String TAG,String msg){
        if(status>=RELEASE){
            Log.e(TAG, msg);
        }
    }

}
