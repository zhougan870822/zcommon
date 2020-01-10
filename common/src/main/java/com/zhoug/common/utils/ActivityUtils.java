package com.zhoug.common.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivityUtils {

    /**
     * 启动activity
     * @param context
     * @param cls
     */
    public static void startActivity(Context context, Class cls){
        Intent intent=new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 启动activity
     * @param context
     * @param cls
     * @param bundle
     */
    public static void startActivity(Context context, Class cls, Bundle bundle){
        Intent intent=new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


}
