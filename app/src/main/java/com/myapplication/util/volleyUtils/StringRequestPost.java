package com.myapplication.util.volleyUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.myapplication.base.Constants;

import java.util.HashMap;
import java.util.Map;

public class StringRequestPost extends StringRequest {

    private static Map<String, String> param = new HashMap<String, String>();

    public StringRequestPost(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringRequestPost(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public StringRequestPost(String url, Response.Listener<String> listener,
                             StrErrListener errorListener, Map<String, String> params) {
        this(Method.POST, url, listener, errorListener);
        param = params;
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
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (param != null) {
            params = param;
        }
        return params;
    }
}
