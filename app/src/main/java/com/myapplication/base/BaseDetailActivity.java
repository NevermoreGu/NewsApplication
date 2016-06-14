package com.myapplication.base;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.myapplication.R;
import com.myapplication.bean.Entity;
import com.myapplication.cache.CacheManager;
import com.myapplication.util.TDevice;
import com.myapplication.util.volleyUtils.StrErrListener;
import com.myapplication.widget.EmptyLayout;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.InjectView;

public abstract class BaseDetailActivity<T extends Entity> extends BaseActivity {

    @InjectView(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;

    private AsyncTask<String, Void, T> mCacheTask;
    private ParserTask mParserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData();
            }
        });
        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
        requestData();
    }

    protected void requestData() {
        String key = getCacheKey();
        if (isReadCacheData()) {
            readCacheData(key);
        } else {
            // 取新的数据
            sendRequestData();
        }
    }

    private String getCacheKey() {
        return getCacheKeyPrefix();
    }

    protected String getCacheKeyPrefix() {
        return "头条";
    }

    /**
     * 判断是否需要读取缓存的数据
     *
     * @return
     */
    protected boolean isReadCacheData() {
        if (!TDevice.hasInternet()) {
            return true;
        }

        return false;
    }

    protected void sendRequestData() {

    }

    protected T parseData(String string) throws Exception {
        return null;
    }

    protected T readData(Serializable serializable) {
        return null;
    }

    /**
     * 读取缓存
     *
     * @param cacheKey
     */
    private void readCacheData(String cacheKey) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(this).execute(cacheKey);
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private class CacheTask extends AsyncTask<String, Void, T> {

        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected T doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable seri = CacheManager.readObject(mContext.get(),
                        params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return readData(seri);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(T data) {
            super.onPostExecute(data);
            if (data != null) {
                executeOnLoadDataSuccess(data);
            } else {
                executeOnLoadDataError(null);
            }
        }
    }

    /**
     * 写入缓存
     */
    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    protected Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            onRefreshNetworkSuccess();
            executeParserListTask(response);
        }
    };

    protected Response.ErrorListener errorListener = new StrErrListener() {
        @Override
        public void onErrorResponse(VolleyError arg0) {

            readCacheData(getCacheKey());

        }
    };

    /**
     * 处理联网请求得到的数据
     *
     * @param data
     */
    private void executeParserListTask(String data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    class ParserTask extends AsyncTask<Void, Void, String> {

        private final String reponseData;
        private boolean parserError;
        private T data;

        public ParserTask(String data) {
            this.reponseData = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                data = parseData(reponseData);
                new SaveCacheTask(BaseDetailActivity.this, (Serializable) data, getCacheKey()).execute();

            } catch (Exception e) {
                e.printStackTrace();

                parserError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (parserError || data == null) {
                readCacheData(getCacheKey());
            } else {
                executeOnLoadDataSuccess(data);
            }
        }
    }

    protected void executeOnLoadDataSuccess(T data) {
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
    }

    protected void executeOnLoadDataError(String error) {
        if (!CacheManager.isExistDataCache(this, getCacheKey())) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }
    }

    protected boolean needShowEmptyNoData() {
        return true;
    }

    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();
        if (enity != null) {
            for (int i = 0; i < s; i++) {
                if (enity.getId() == data.get(i).getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void onRefreshNetworkSuccess() {

    }

    @Override
    public void onDestroy() {
        cancelReadCacheTask();
        cancelParserTask();
        super.onDestroy();
    }
}
