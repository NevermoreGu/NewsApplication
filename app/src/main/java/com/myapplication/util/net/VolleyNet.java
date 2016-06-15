package com.myapplication.util.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.myapplication.bean.Entity;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class VolleyNet<T extends Entity> {

    private static VolleyNet mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mCtx;
    private Gson mJson;

    private VolleyNet(Context context) {
        mCtx = context.getApplicationContext();
        mJson = new Gson();
        mRequestQueue = getRequestQueue();
        final int maxMemory = ((int) Runtime.getRuntime().maxMemory()) / 1024 / 1024;
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(
                            maxMemory / 8) {
                        protected int sizeOf(String key, Bitmap bitmap) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                                return bitmap.getByteCount() / 1024;
                            } else {
                                return (bitmap.getRowBytes() * bitmap
                                        .getHeight()) / 1024;
                            }
                        }
                    };

                    @Override
                    public Bitmap getBitmap(String url) {

                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        if (cache.get(url) == null) {
                            cache.put(url, bitmap);
                        }
                    }


                });
    }

    public static VolleyNet getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyNet(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx);
        }
        return mRequestQueue;
    }

    public void addToRequestQueue(String tag, final NetBuilder<T> netBuilder) {
        final NetCallBack<T> netCallBack = netBuilder.callBack;
        String url = netBuilder.url;
        String param = netBuilder.param;
        final Class<T> clazz = netBuilder.clazz;
        final Type type = netBuilder.type;

        netCallBack.onStart();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                T result = null;
                try {
                    if (clazz != null) {
                        result = mJson.fromJson(response.toString(), clazz);
                    } else if (type != null) {
                        result = mJson.fromJson(response.toString(), type);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                netCallBack.onResponse(result);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netCallBack.onErrorResponse(error);

            }
        };

        JsonObjectRequest request;
        if (param != null) {
            request = new JsonObjectRequest(Request.Method.POST, url, param, listener, errorListener){
                @Override
                protected void onFinish() {
                    super.onFinish();
                    netCallBack.onFinish();
                }
            };
        } else {
            request = new JsonObjectRequest(url, listener, errorListener){
                @Override
                protected void onFinish() {
                    super.onFinish();
                    netCallBack.onFinish();
                }
            };
        }
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        getRequestQueue().cancelAll(tag);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}