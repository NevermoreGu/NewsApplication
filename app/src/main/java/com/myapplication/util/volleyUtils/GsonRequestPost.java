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
import java.util.HashMap;
import java.util.Map;

public class GsonRequestPost<T> extends Request<T> {

	private final Listener<T> mListener;

	private Gson mGson;

	private Class<T> mClass;
	private static Map<String, String> param = new HashMap<String, String>();

	public GsonRequestPost(int method, String url, Class<T> clazz,
			Listener<T> listener, StrErrListener errorListener,
			Map<String, String> params) {
		super(method, url, errorListener);
		mGson = new Gson();
		mClass = clazz;
		mListener = listener;
		param = params;
	}

	public GsonRequestPost(String url, Class<T> clazz, Listener<T> listener,
			StrErrListener errorListener, Map<String, String> params) {
		this(Method.POST, url, clazz, listener, errorListener, params);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(mGson.fromJson(jsonString, mClass),
					HttpHeaderParser.parseCacheHeaders(response));
 
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
	protected Map<String, String> getParams() {
		Map<String, String> params = new HashMap<String, String>();
		if (param != null) {
			params = param;
		}
		return params;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

}