package com.myapplication.util.volleyUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.myapplication.base.Constants;

import java.util.HashMap;
import java.util.Map;

public class StringRequestGet extends StringRequest {

    public StringRequestGet(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringRequestGet(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
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
}
