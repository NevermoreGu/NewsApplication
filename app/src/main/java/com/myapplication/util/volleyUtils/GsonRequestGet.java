package com.myapplication.util.volleyUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.myapplication.base.Constants;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonRequestGet<T> extends Request<T> {

    private final Listener<T> mListener;

    private Gson mGson;

    private Class<T> mClass;

    private Type mType;

    public GsonRequestGet(int method, String url, Class<T> clazz,
                          Listener<T> listener, StrErrListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        mClass = clazz;
        mListener = listener;
    }

    public GsonRequestGet(int method, String url, Type type,
                          Listener<T> listener, StrErrListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        mType = type;
        mListener = listener;
    }

    public GsonRequestGet(String url, Class<T> clazz, Listener<T> listener,
                          StrErrListener errorListener) {

        this(Method.GET, url, clazz, listener, errorListener);
    }

    public GsonRequestGet(String url, Type type, Listener<T> listener,
                          StrErrListener errorListener) {

        this(Method.GET, url, type, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (mClass != null) {
                return (Response<T>) Response.success(mGson.fromJson(jsonString, mClass),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else if (mType != null) {
                return (Response<T>) Response.success(
                        mGson.fromJson(jsonString, mType),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
            return null;

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("token", Constants.APPTOKEN);
        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}