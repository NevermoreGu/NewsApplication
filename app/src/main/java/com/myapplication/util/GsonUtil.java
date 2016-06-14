package com.myapplication.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonUtil {

    private GsonUtil gsonUtil;

    private 

    Gson gson = new GsonBuilder()
            //序列化null
            .serializeNulls()
            .create();

    public static <T> T toBean(String response, Class<T> mClass) {
        Gson mGson = new Gson();
        T obj = null;
        obj = mGson.fromJson(response, mClass);
        return obj;
    }

    public static <T> T toBean(String response, Type type) {
        Gson mGson = new Gson();
        T obj = null;
        obj = mGson.fromJson(response, type);
        return obj;
    }

    public static String toJson(Object object) {
        Gson mGson = new Gson();
        return mGson.toJson(object);
    }

    public static String toJson(Object object, Type type) {
        Gson mGson = new Gson();
        return mGson.toJson(object, type);
    }

}
