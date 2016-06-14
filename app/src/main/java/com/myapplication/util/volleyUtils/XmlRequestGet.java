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
import com.myapplication.base.Constants;
import com.myapplication.util.XmlUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class XmlRequestGet<T> extends Request<T> {

    private final Listener<T> mListener;

    private Class<T> mClass;

    public XmlRequestGet(int method, String url, Class<T> clazz,
                         Listener<T> listener, StrErrListener errorListener) {
        super(method, url, errorListener);
        mClass = clazz;
        mListener = listener;
    }

    public XmlRequestGet(int method, String url, Type type,
                         Listener<T> listener, StrErrListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public XmlRequestGet(String url, Class<T> clazz, Listener<T> listener,
                         StrErrListener errorListener) {

        this(Method.GET, url, clazz, listener, errorListener);
    }

    public XmlRequestGet(String url, Type type, Listener<T> listener,
                         StrErrListener errorListener) {

        this(Method.GET, url, type, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            byte[] bytes = jsonString.getBytes();
            if (mClass != null) {
                return (Response<T>) Response.success(XmlUtils.toBean(mClass, bytes),
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