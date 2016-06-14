package com.myapplication.base;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.myapplication.R;
import com.myapplication.bean.Entity;
import com.myapplication.bean.Result;
import com.myapplication.cache.CacheManager;
import com.myapplication.ui.Drawable.RentalsSunHeaderView;
import com.myapplication.util.TDevice;
import com.myapplication.util.volleyUtils.StrErrListener;
import com.myapplication.widget.EmptyLayout;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public abstract class BaseListFragment<T extends Entity> extends BaseFragment
        implements AdapterView.OnItemClickListener{

    @InjectView(R.id.refresh_layout)
    protected PtrFrameLayout mPtrFrameLayout;

    @InjectView(R.id.abs_list_view)
    protected ListView mListView;

    protected BaseListAdapter<T> mAdapter;

    @InjectView(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;

    protected int mCurrentPage = 1;

    // 错误信息
    protected Result mResult;

    private AsyncTask<String, Void, List<T>> mCacheTask;
    private ParserListTask mParserListTask;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
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
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 100);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentPage = 1;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestListData();
            }
        });

        mListView.setOnItemClickListener(this);
//        mListView.setOnScrollListener(this);

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);

            if (requestDataIfViewCreated()) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                mPtrFrameLayout.setPtrHandler(new PtrHandler() {
                    @Override
                    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
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
        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
    }

    protected boolean requestDataIfViewCreated() {
        return true;
    }

    protected void requestListData() {
        String key = getListCacheKey();
        if (isReadCacheData()) {
            //读取缓存
            readListCacheData(key);
        } else {
            // 取新的数据
            sendListRequestData();
        }
    }

    protected abstract BaseListAdapter<T> getListAdapter();

    protected String getListCacheKey() {
        return new StringBuilder(getListCacheKeyPrefix()).append("_")
                .append(mCurrentPage).toString();
    }

    protected String getListCacheKeyPrefix() {
        return "";
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

    protected List<T> parseList(String string) throws Exception {
        return null;
    }

    protected List<T> readList(Serializable serializable) {
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
    protected void readListCacheData(String cacheKey) {
        cancelReadListCacheTask();
        mCacheTask = new CacheListTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadListCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private class CacheListTask extends AsyncTask<String, Void, List<T>> {

        private final WeakReference<Context> mContext;

        private CacheListTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected List<T> doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable seri = CacheManager.readObject(mContext.get(),
                        params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return readList(seri);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<T> list) {
            super.onPostExecute(list);
            if (list != null) {
                executeOnLoadListDataSuccess(list);
            } else {
                executeOnLoadListDataError(null);
            }
            executeOnLoadListFinish();
        }
    }

    /**
     * 写入缓存
     */
    protected class SaveListCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveListCacheTask(Context context, Serializable seri, String key) {
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

            if (mCurrentPage == 0 && needAutoRefresh()) {
//                AppContext.putToLastRefreshTime(getCacheKey(),
//                        StringUtils.getCurTimeStr());
            }
            if (isAdded()) {
                executeParserListTask(response);
            }
        }
    };

    protected Response.ErrorListener errorListener = new StrErrListener() {
        @Override
        public void onErrorResponse(VolleyError arg0) {

            if (isAdded()) {
                readListCacheData(getListCacheKey());
            }
        }
    };

    /**
     * 处理联网请求得到的数据
     *
     * @param data
     */
    protected void executeParserListTask(String data) {
        cancelParserListTask();
        mParserListTask = new ParserListTask(data);
        mParserListTask.execute();
    }

    private void cancelParserListTask() {
        if (mParserListTask != null) {
            mParserListTask.cancel(true);
            mParserListTask = null;
        }
    }

    class ParserListTask extends AsyncTask<Void, Void, String> {

        private final String reponseData;
        private boolean parserError;
        private List<T> list;

        public ParserListTask(String data) {
            this.reponseData = data;
            list = new ArrayList<>();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                List<T> data = parseList(reponseData);
//                new SaveListCacheTask(getActivity(), (Serializable) data, getListCacheKey()).execute();
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
                readListCacheData(getListCacheKey());
            } else {
                executeOnLoadListDataSuccess(list);
                executeOnLoadListFinish();
            }
        }
    }

    protected void executeOnLoadListDataSuccess(List<T> data) {
        if (data == null) {
            data = new ArrayList<T>();
        }

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
        if ((mAdapter.getCount() + data.size()) == 0) {
            adapterState = BaseListAdapter.STATE_EMPTY_ITEM;
        } else if (data.size() == 0
                || (data.size() < getPageSize() && mCurrentPage == 1)) {
            adapterState = BaseListAdapter.STATE_NO_MORE;
            mAdapter.notifyDataSetChanged();
        } else {
            adapterState = BaseListAdapter.STATE_LOAD_MORE;
        }
        mAdapter.setState(adapterState);
//        mAdapter.addData(data);
        // 判断等于是因为最后有一项是listview的状态
        if (mAdapter.getCount() == 1) {

            if (needShowEmptyNoData()) {
                mErrorLayout.setErrorType(EmptyLayout.NO_DATA);
            } else {
                mAdapter.setState(BaseListAdapter.STATE_EMPTY_ITEM);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void executeOnLoadListDataError(String error) {
        if (mCurrentPage == 1
                && !CacheManager.isExistDataCache(getActivity(), getListCacheKey())) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mAdapter.setState(BaseListAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
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
    protected void executeOnLoadListFinish() {
        setSwipeRefreshLoadedState();
    }

    /**
     * 设置顶部正在加载的状态
     */
    private void setSwipeRefreshLoadingState() {
        if (mPtrFrameLayout != null) {
//            refreshLayout.setRefreshing(true);
//            // 防止多次重复刷新
//            refreshLayout.setEnabled(false);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    private void setSwipeRefreshLoadedState() {
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.refreshComplete();
//            refreshLayout.setRefreshing(false);
//            refreshLayout.setEnabled(true);
        }
    }

//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (mAdapter == null || mAdapter.getCount() == 0) {
//            return;
//        }
//        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
//        if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
//            return;
//        }
//        // 判断是否滚动到底部
//        boolean scrollEnd = false;
//        try {
//            if (view.getPositionForView(mAdapter.getFooterView()) == view
//                    .getLastVisiblePosition())
//                scrollEnd = true;
//        } catch (Exception e) {
//            scrollEnd = false;
//        }
//
//        if (mState == STATE_NONE && scrollEnd) {
//            if (mAdapter.getState() == BaseListAdapter.STATE_LOAD_MORE
//                    || mAdapter.getState() == BaseListAdapter.STATE_NETWORK_ERROR) {
//                mCurrentPage++;
//                mState = STATE_LOADMORE;
//                requestListData();
//                mAdapter.setFooterViewLoading();
//            }
//        }
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

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        cancelReadListCacheTask();
        cancelParserListTask();
        super.onDestroy();
    }

}
