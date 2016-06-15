package com.myapplication.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.myapplication.R;
import com.myapplication.bean.Entity;
import com.myapplication.event.EventCenter;
import com.myapplication.event.ListAndHeadDataEvent;
import com.myapplication.model.list.ListPageInfo;
import com.myapplication.ui.refreshmore.CarRefreshHeaderView;
import com.myapplication.widget.EmptyLayout;
import com.squareup.leakcanary.RefWatcher;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public abstract class BaseNewHaveHeadListFragment<T1 extends Entity, T2 extends Entity, T3 extends AbsListView>
        extends BaseFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.refresh_layout)
    protected PtrFrameLayout mPtrFrameLayout;

    @InjectView(R.id.abs_list_view)
    protected T3 absListView;

    @InjectView(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected BaseListAdapter<T1> mAdapter;

    protected int mStoreEmptyState = -1;

    protected String url = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    protected abstract View initHeaderView();

    protected void initRootView() {
    }

    protected abstract BaseListAdapter<T1> getListAdapter();

    protected String getCacheKey() {
        return new StringBuilder(getCacheKeyPrefix()).append("_")
                .append(/*mCurrentPage*/"").toString();
    }

    protected abstract String getCacheKeyPrefix();

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

//        final RentalsSunHeaderView header = new RentalsSunHeaderView(getActivity());
//        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
//        header.setPadding(0, (int) TDevice.dpToPixel(15), 0, (int) TDevice.dpToPixel(10));
//        header.setUp(mPtrFrameLayout);
        CarRefreshHeaderView header = new CarRefreshHeaderView(getActivity());

        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.disableWhenHorizontalMove(true);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                sendListRequestData();
            }
        });

        absListView.setOnItemClickListener(this);

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
                        sendListRequestData();
                    }
                });

            } else {
                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            }
        }

        sendListRequestData();
        EventCenter.getInstance().register(this);
    }

    public void onEventMainThread(ListAndHeadDataEvent event) {
        if (event.url.equals(url)) {
            mPtrFrameLayout.refreshComplete();
            ListPageInfo<T1> listPageInfo = event.listPageInfo;
            if (event.isError == false) {
                T2 data = (T2) event.data;
                if (listPageInfo.isFirstPage()) {
                    if (data == null) {
                        mErrorLayout.setErrorType(EmptyLayout.NO_DATA);
                    } else {
                        executeDataSuccess(data);
                        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    }
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                }
                mAdapter.notifyDataSetChanged();
                updateView(false, event.hasMore);
            } else {
                if (listPageInfo.isFirstPage()) {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                } else {
                    mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                }
                updateErrorView(0, "");
            }
        }
    }


//    EventCenter.bindContainerAndHandler(this,new
//
//    SimpleEventHandler() {
//
//        public void onEvent (ListAndHeadDataEvent < T1, T2 > event){
//            mPtrFrameLayout.refreshComplete();
//
//            // load more complete
////                loadMoreContainer.loadMoreFinish(mDataModel.getListPageInfo().isEmpty(), mDataModel.getListPageInfo().hasMore());
//
//            mAdapter.notifyDataSetChanged();
//        }
//
//    }
//
//    ).
//
//    tryToUnregister();

//}

    protected boolean requestDataIfViewCreated() {
        return true;
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

    protected T2 readData(Serializable serializable) {
        return null;
    }

    protected void executeDataSuccess(T2 data) {
    }

    protected void updateView(boolean emptyResult, boolean hasMore) {
    }

    protected void updateErrorView(int errorCode, String errorMessage) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    protected int getHeadDataCount() {
        return 0;
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

    @Override
    public void onStop() {
        EventCenter.getInstance().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
        super.onDestroy();
    }

}
