package com.myapplication.base;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.myapplication.bean.Entity;
import com.myapplication.cache.CacheManager;
import com.myapplication.util.volleyUtils.StrErrListener;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseHaveHeaderListFragment<T1 extends Entity, T2 extends Entity>
        extends BaseListFragment<T1> {

    protected Activity aty;

    private AsyncTask<String, Void, List<T2>> mHeadCacheTask;
    private ParserHeadTask mParserHeadTask;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.inject(this, view);
        mListView.addHeaderView(initHeaderView());
        aty = getActivity();
        super.initView(view);
        requestHeadData();
    }

    protected abstract View initHeaderView();

    protected abstract String getHeadCacheKey();

    protected List<T2> parseHeadList(String string) throws Exception {
        return null;
    }

    @Override
    protected boolean requestDataIfViewCreated() {
        return true;
    }

    /**
     * 带有header view的listfragment不需要显示是否数据为空
     */
    @Override
    protected boolean needShowEmptyNoData() {
        return false;
    }

    protected void requestHeadData() {
        String key = getHeadCacheKey();
        if (isReadCacheData()) {
            readHeadCacheData(key);
        } else {
            // 取新的数据
            sendHeadRequestData();
        }
    }

    protected void sendHeadRequestData() {

    }

    /**
     * 读取缓存
     */
    private void readHeadCacheData(String cacheKey) {
        cancelReadHeadCacheTask();
        mHeadCacheTask = new ReadHeadCacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadHeadCacheTask() {
        if (mHeadCacheTask != null) {
            mHeadCacheTask.cancel(true);
            mHeadCacheTask = null;
        }
    }

    private class ReadHeadCacheTask extends AsyncTask<String, Void, List<T2>> {
        private final WeakReference<Context> mContext;

        private ReadHeadCacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected List<T2> doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable seri = CacheManager.readObject(mContext.get(),
                        params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return (List<T2>) seri;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<T2> list) {
            super.onPostExecute(list);
            if (list != null) {
                executeOnLoadHeadDataSuccess(list);
            } else {
                executeOnLoadHeadDataError(null);
            }
            executeOnLoadHeadDataFinish();
        }
    }

    /**
     * 写入缓存
     */
    private class SaveHeadCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveHeadCacheTask(Context context, Serializable seri, String key) {
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


    protected Response.Listener<String> headListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            if (isAdded()) {

                executeParserHeadTask(response);
            }
        }
    };

    protected Response.ErrorListener headErrorListener = new StrErrListener() {
        @Override
        public void onErrorResponse(VolleyError arg0) {

            if (isAdded()) {
                readHeadCacheData(getHeadCacheKey());
            }
        }
    };

    /**
     * 处理联网请求得到的数据
     *
     * @param data
     */
    private void executeParserHeadTask(String data) {
        cancelParserHeadTask();
        mParserHeadTask = new ParserHeadTask(data);
        mParserHeadTask.execute();
    }

    private void cancelParserHeadTask() {
        if (mParserHeadTask != null) {
            mParserHeadTask.cancel(true);
            mParserHeadTask = null;
        }
    }

    class ParserHeadTask extends AsyncTask<Void, Void, String> {

        private final String reponseData;
        private boolean parserError;
        private List<T2> list;

        public ParserHeadTask(String data) {
            this.reponseData = data;
            list = new ArrayList<>();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                List<T2> data = parseHeadList(reponseData);
                new SaveHeadCacheTask(getActivity(), (Serializable) data,
                        getHeadCacheKey()).execute();
                list.addAll(data);
//                if (list == null) {
//                    ResultBean resultBean = XmlUtils.toBean(ResultBean.class,
//                            reponseData);
//                    if (resultBean != null) {
//                        mResult = resultBean.getResult();
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();

                parserError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (parserError) {
                readHeadCacheData(getHeadCacheKey());
            } else {
                executeOnLoadHeadDataSuccess(list);
                executeOnLoadHeadDataFinish();
            }
        }
    }

    protected abstract void executeOnLoadHeadDataSuccess(List<T2> detailBean);

    protected void executeOnLoadHeadDataError(String error) {

    }

    protected void executeOnLoadHeadDataFinish() {

    }

    protected <T extends View> T findHeaderView(View headerView, int viewId) {
        return (T) headerView.findViewById(viewId);
    }

    @Override
    public void onDestroy() {
        cancelReadHeadCacheTask();
        cancelParserHeadTask();
        super.onDestroy();
    }
}
