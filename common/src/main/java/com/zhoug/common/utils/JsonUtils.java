package com.zhoug.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @Author HK-LJJ
 * @Date 2019/11/27
 * @Description
 */
public class JsonUtils {
    public static String toJson(Object obj){
        return new Gson().toJson(obj);
    }

    public static String toJsonWithoutExpose(Object obj){
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(obj);
    }

    public static <T> T fromJson(String json,Class<T> cls){
        return new Gson().fromJson(json,cls);
    }

    public static <T> T fromJsonList(String json,Class<T> cls){
        Type type=new TypeToken<List<T>>(){}.getType();
        return new Gson().fromJson(json,type);
    }
}
