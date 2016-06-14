package com.myapplication.base;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.myapplication.R;
import com.myapplication.bean.Entity;
import com.myapplication.bean.Result;
import com.myapplication.cache.CacheManager;
import com.myapplication.event.EventCenter;
import com.myapplication.event.SimpleEventHandler;
import com.myapplication.ui.Drawable.RentalsSunHeaderView;
import com.myapplication.util.TDevice;
import com.myapplication.util.volleyUtils.StrErrListener;
import com.myapplication.widget.EmptyLayout;
import com.myapplication.widget.GridViewWithHeaderAndFooter;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public abstract class BaseHaveHeadListFragment<T1 extends Entity, T2 extends Entity, T3 extends AbsListView>
        extends BaseFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.refresh_layout)
    protected PtrFrameLayout mPtrFrameLayout;

    @InjectView(R.id.abs_list_view)
    protected T3 absListView;

    protected BaseListAdapter<T1> mAdapter;

    @InjectView(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;

    protected int mCurrentPage = 1;

    // 错误信息
    protected Result mResult;

    private AsyncTask<String, Void, T2> mCacheTask;
    private ParserTask mParserTask;
    private List<T1> list;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    protected abstract View initHeaderView();

    protected void initRootView(){}

    protected <T extends View> T findHeaderView(View headerView, int viewId) {
        return (T) headerView.findViewById(viewId);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        initHeaderView();
        initRootView();
        initView(view);
    }

    @Override
    public void initView(View view) {

        final RentalsSunHeaderView header = new RentalsSunHeaderView(getActivity());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, (int) TDevice.dpToPixel(15), 0, (int) TDevice.dpToPixel(10));
        header.setUp(mPtrFrameLayout);

        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.disableWhenHorizontalMove(true);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentPage = 1;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestListData();
            }
        });

        absListView.setOnItemClickListener(this);
//        absListView.setOnScrollListener(this);

        if (mAdapter != null) {
            absListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            absListView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                /**
                 * 下拉刷新的时候才会执行
                 */
                mPtrFrameLayout.setPtrHandler(new PtrHandler() {
                    @Override
                    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                        return PtrDefaultHandler.checkContentCanBePulledDown(frame, absListView, header);
                    }

                    @Override
                    public void onRefreshBegin(final PtrFrameLayout frame) {
                        requestListData();
                    }
                });

            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }

        }
//        mPtrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrameLayout.autoRefresh(false);
//            }
//        }, 100);
        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
        //加载更多
        if (absListView instanceof ListView) {


        } else if (absListView instanceof GridViewWithHeaderAndFooter) {

        }
        requestListData();
//        EventCenter.bindContainerAndHandler(this,new SimpleEventHandler(){
//            public void onEvent(){
//
//            }
//
//        }).tryToUnregister();
    }

    protected boolean requestDataIfViewCreated() {
        return true;
    }

    protected void requestListData() {

        String key = getCacheKey();
        if (isReadCacheData()) {
            readCacheData(key);
        } else {
            // 取新的数据
            sendListRequestData();
        }
    }

    protected abstract BaseListAdapter<T1> getListAdapter();

    protected String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(mCurrentPage).toString();
    }

    protected abstract String getCacheKeyPrefix();

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

    // 是否需要自动刷新
    protected boolean needAutoRefresh() {
        return true;
    }

    /**
     * 自动刷新的时间
     * 默认：自动刷新的时间为半天时间
     */
    protected long getAutoRefreshTime() {
        return 12 * 60 * 60;
    }

    protected void sendListRequestData() {

    }

    protected T2 parseData(String is) throws Exception {
        return null;
    }

    protected List<T1> parseList() throws Exception {
        return null;
    }

    protected T2 readData(Serializable serializable) {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 读取缓存
     *
     * @param cacheKey
     */
    protected void readCacheData(String cacheKey) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private class CacheTask extends AsyncTask<String, Void, T2> {

        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected T2 doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable serializable = CacheManager.readObject(mContext.get(),
                        params[0]);
                if (serializable == null) {
                    return null;
                } else {
                    return readData(serializable);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(T2 data) {
            super.onPostExecute(data);
            if (data != null) {
                executeOnLoadDataSuccess(data);
            } else {
                executeOnLoadDataError(null);
            }
            executeOnLoadFinish();
        }
    }

    /**
     * 写入缓存
     */
    protected class SaveCacheTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Context> mContext;
        private final Serializable serializable;
        private final String key;

        private SaveCacheTask(Context context, Serializable serializable, String key) {
            mContext = new WeakReference<>(context);
            this.serializable = serializable;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), serializable, key);
            return null;
        }
    }

    protected Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            if (mCurrentPage == 0 && needAutoRefresh()) {
//                AppContext.putToLastRefreshTime(getCacheKey(),
//                        StringUtils.getCurTimeStr());
            }
            if (isAdded()) {
                executeParserTask(response);
            }
        }
    };

    protected Response.ErrorListener errorListener = new StrErrListener() {
        @Override
        public void onErrorResponse(VolleyError arg0) {

            if (isAdded()) {
                readCacheData(getCacheKey());
            }
        }
    };

    /**
     * 处理联网请求得到的数据
     *
     * @param data
     */
    protected void executeParserTask(String data) {
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

        private final String responseData;
        private boolean parserError;
        private T2 data;

        public ParserTask(String data) {
            this.responseData = data;
            list = new ArrayList<>();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                data = parseData(responseData);
                List<T1> listData = parseList();
                list.addAll(listData);
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
                readCacheData(getCacheKey());
            } else {
                executeOnLoadDataSuccess(data);

            }
            executeOnLoadFinish();
        }
    }

    protected void executeOnLoadDataSuccess(T2 data) {
        if (data == null) {
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
            return;
        }
        executeDataSuccess(data);
//        if (mResult != null && !mResult.OK()) {
//            AppContext.showToast(mResult.getErrorMessage());
//            // 注销登陆，密码已经修改，cookie，失效了
//            AppContext.getInstance().Logout();
//        }
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
//        if (mCurrentPage == 1) {
//            mAdapter.clear();
//        }

//        for (int i = 0; i < data.size(); i++) {
//            if (compareTo(mAdapter.getData(), data.get(i))) {
//                data.remove(i);
//                i--;
//            }
//        }
        int adapterState = BaseListAdapter.STATE_EMPTY_ITEM;
        if ((mAdapter.getCount() + list.size()) == 0) {
            adapterState = BaseListAdapter.STATE_EMPTY_ITEM;
        } else if (list.size() == 0
                || (list.size() + getHeadDataCount() < getPageSize() && mCurrentPage == 1)) {
            adapterState = BaseListAdapter.STATE_NO_MORE;
            mAdapter.notifyDataSetChanged();
        } else {
            adapterState = BaseListAdapter.STATE_LOAD_MORE;
        }
        mAdapter.setState(adapterState);
//        mAdapter.addData(list);
        // 判断等于是因为最后有一项是listview的状态
//        if (mAdapter.getCount() == 1) {
//
//            if (needShowEmptyNoData()) {
//                mErrorLayout.setErrorType(EmptyLayout.NODATA);
//            } else {
//                mAdapter.setState(BaseListAdapter.STATE_EMPTY_ITEM);
//                mAdapter.notifyDataSetChanged();
//            }
//        }

    }

    protected int getHeadDataCount() {
        return 0;
    }

    protected void executeOnLoadDataError(String error) {
        if (mCurrentPage == 1
                && !CacheManager.isExistDataCache(getActivity(), getCacheKey())) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mAdapter.setState(BaseListAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected void executeDataSuccess(T2 data) {

    }

    /**
     * 是否需要隐藏listview，显示无数据状态
     */
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

    protected int getPageSize() {
        return Constants.PAGE_SIZE;
    }

    protected void onRefreshNetworkSuccess() {
    }

    // 完成刷新
    protected void executeOnLoadFinish() {
        setRefreshLoadedState();
    }

    /**
     * 设置顶部加载完毕的状态
     */
    private void setRefreshLoadedState() {
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.refreshComplete();
        }
    }

//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /*if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
            return;
        }
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                onListViewScrollStop();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

                break;
        }
        // 判断是否滚动到底部
        boolean scrollEnd = false;
        try {
            if (view.getPositionForView(mAdapter.getFooterView()) == view
                    .getLastVisiblePosition())
                scrollEnd = true;
        } catch (Exception e) {
            scrollEnd = false;
        }

        if (mState == STATE_NONE && scrollEnd) {
            if (mAdapter.getState() == BaseListAdapter.STATE_LOAD_MORE
                    || mAdapter.getState() == BaseListAdapter.STATE_NETWORK_ERROR) {
                mCurrentPage++;
                mState = STATE_LOADMORE;
                requestListData();
                mAdapter.setFooterViewLoading();
            }
        }*/
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem,
//                         int visibleItemCount, int totalItemCount) {
    // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
//        if (mState == STATE_NOMORE || mState == STATE_LOADMORE
//                || mState == STATE_REFRESH) {
//            return;
//        }
//        if (mAdapter != null
//                && mAdapter.getDataSize() > 0
//                && mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
//            if (mState == STATE_NONE
//                    && mAdapter.getState() == BaseListAdapter.STATE_LOAD_MORE) {
//                mState = STATE_LOADMORE;
//                mCurrentPage++;
//                requestListData();
//            }
//        }
//    }

    protected void onListViewScrollStop() {

    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        cancelReadCacheTask();
        cancelParserTask();
        super.onDestroy();
    }

}
