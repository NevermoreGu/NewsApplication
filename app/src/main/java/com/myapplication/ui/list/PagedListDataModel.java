package com.myapplication.ui.list;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.myapplication.event.EventCenter;
import com.myapplication.event.ListDataEvent;
import com.myapplication.util.TDevice;
import com.myapplication.util.volleyUtils.StrErrListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public abstract class PagedListDataModel<T> {

    protected ListPageInfo<T> mListPageInfo;//由子类实现
    protected List<T> listData; //解析的数据
    protected String mUrlPart; //交由子类实现

    private PagedListDataHandler mPagedListDataHandler;

    public interface PagedListDataHandler {
        void onPageDataLoaded(ListPageInfo<?> listPageInfo);
    }

    public void setPageListDataHandler(PagedListDataHandler handler) {
        mPagedListDataHandler = handler;
    }

    protected abstract String getCacheKeyPrefix();

    protected String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mListPageInfo.getPage()).toString();
    }

    /**
     * 判断是否需要读取缓存的数据
     *
     * @return
     */
    protected boolean isReadCacheData() {
        if (!TDevice.hasInternet()) {
            return true; //读取缓存
        }
        return false;  //联网请求数据
    }

    protected abstract void doQueryData();//联网请求数据

    protected Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            onRequestFinish(response);
        }
    };

    protected Response.ErrorListener errorListener = new StrErrListener() {
        @Override
        public void onErrorResponse(VolleyError arg0) {
            onRequestFail();
        }
    };

    protected void onRequestFinish(String response) {

        listData = parseListData(response);

        boolean mHasMore = true;
        if (listData != null && listData.size() < mListPageInfo.getNumPerPage()) {
            mHasMore = false;
        }
        ListDataEvent<T> event = new ListDataEvent<T>();
        event.hasMore = mHasMore;
        event.newsList = listData;
        event.url = mUrlPart;
        event.listPageInfo = mListPageInfo;
        setRequestResult(listData, mHasMore);
        EventCenter.getInstance().post(event);
    }

    protected void onRequestFail() {
    }

    protected void readCacheData() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
        });
    }

    protected void saveCacheData() {

    }

    /**
     * 由子类实现
     *
     * @param is
     * @return
     */
    protected ArrayList<T> parseListData(String is) {
        return null;
    }

    public void queryFirstPage() {
        checkPageInfo();
        mListPageInfo.goToHead();
        doQueryDataInner();
    }

    public void queryNextPage() {
        checkPageInfo();
        if (mListPageInfo.prepareForNextPage()) {
            doQueryDataInner();
        }
    }

    private void checkPageInfo() {
        if (mListPageInfo == null) {
            throw new IllegalArgumentException(" mListPageInfo has not been initialized.");
        }
    }

    private void doQueryDataInner() {
        if (!mListPageInfo.tryEnterLock()) {
            return;
        }
        if (isReadCacheData()) {


        } else {
            doQueryData();
        }
    }

    protected void setRequestResult(List<T> list) {
        mListPageInfo.updateListInfo(list);
        if (null != mPagedListDataHandler) {
            mPagedListDataHandler.onPageDataLoaded(mListPageInfo);
        }
    }

    protected void setRequestResult(List<T> list, int total) {
        mListPageInfo.updateListInfo(list, total);
        if (null != mPagedListDataHandler) {
            mPagedListDataHandler.onPageDataLoaded(mListPageInfo);
        }
    }

    protected void setRequestResult(List<T> list, boolean hasMore) {
        mListPageInfo.updateListInfo(list, hasMore);
        if (null != mPagedListDataHandler) {
            mPagedListDataHandler.onPageDataLoaded(mListPageInfo);
        }
    }

    protected void setRequestFail() {
        mListPageInfo.rollbackOnFail();
    }

    public ListPageInfo<T> getListPageInfo() {
        return mListPageInfo;
    }
}