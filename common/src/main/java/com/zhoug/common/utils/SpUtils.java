package com.zhoug.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 描述：SharedPreferences
 * zhougan
 * 2019/3/24
 **/
public class SpUtils {
    private static final String TAG = "SpUtils";

    private static Context appContext;

    /**
     * 初始化,请在Application中使用
     * @param context
     */
    public static void init(Context context){
        appContext=context;
    }

    /**
     *
     * @param name 不要xml后缀,如果name为null,获取默认的:PreferenceManager.getDefaultSharedPreferences(context)
     * @return
     */
    public static SharedPreferences getSharedPreferences(String name){
        if(appContext==null){
            appContext=AppUtils.getApplicationByReflect();
        }
        if(StringUtils.isEmpty(name)){
            return PreferenceManager.getDefaultSharedPreferences(appContext);
        }else{
            return appContext.getSharedPreferences(name,Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getSharedPreferences(Context context,String name){
        if(appContext==null){
            appContext=context.getApplicationContext();
        }
        if(StringUtils.isEmpty(name)){
            return PreferenceManager.getDefaultSharedPreferences(appContext);
        }else{
            return appContext.getSharedPreferences(name,Context.MODE_PRIVATE);
        }
    }

    public static <T> void put(SharedPreferences sp,String key,T value){
        SharedPreferences.Editor edit = sp.edit();
        if(value instanceof Integer){
            edit.putInt(key, (Integer) value);
        }else if(value instanceof String){
            edit.putString(key, (String) value);
        }else if(value instanceof Boolean){
            edit.putBoolean(key, (Boolean) value);
        }else if(value instanceof Float){
            edit.putFloat(key, (Float) value);
        }else if(value instanceof Long){
            edit.putLong(key, (Long) value);
        }else if(value instanceof Set){
            edit.putStringSet(key, (Set<String>) value);
        }else{
            Log.e(TAG, "不支持给定的value类型");
        }
        edit.apply();
    }

    public static <T> void put(SharedPreferences sp, Map<String,T> map){
        SharedPreferences.Editor edit = sp.edit();

        Set<Map.Entry<String, T>> entries = map.entrySet();
        Iterator<Map.Entry<String, T>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, T> next = iterator.next();
            T value = next.getValue();
            if(value instanceof Integer){
                edit.putInt(next.getKey(), (Integer) value);
            }else if(value instanceof String){
                edit.putString(next.getKey(), (String) value);
            }else if(value instanceof Boolean){
                edit.putBoolean(next.getKey(), (Boolean) value);
            }else if(value instanceof Float){
                edit.putFloat(next.getKey(), (Float) value);
            }else if(value instanceof Long){
                edit.putLong(next.getKey(), (Long) value);
            }else if(value instanceof Set){
                edit.putStringSet(next.getKey(), (Set<String>) value);
            }else{
                Log.e(TAG, "不支持给定的value类型");
            }
        }
        edit.apply();
    }

}
