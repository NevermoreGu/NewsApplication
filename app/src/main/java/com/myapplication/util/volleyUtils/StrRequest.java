package com.myapplication.util.volleyUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.myapplication.base.Constants;

import java.util.HashMap;
import java.util.Map;

public class StrRequest {

    public static Map<String, String> param = new HashMap<String, String>();

    public static StringRequest get(String url, Response.Listener listener, Response.ErrorListener errorListener) {

        StringRequest getRequest = new StringRequest(Request.Method.GET, url, listener, errorListener) {
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
        };
        return getRequest;
    }

    public static StringRequest post(String url, Response.Listener listener, Response.ErrorListener errorListener) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
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
        };
        return postRequest;
    }
}
