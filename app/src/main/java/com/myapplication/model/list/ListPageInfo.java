package com.myapplication.model.list;

import java.util.ArrayList;
import java.util.List;

public class ListPageInfo<T> {

    private int mNumPerPage = 0;//每页的数量条数
    private int mPage = 1; //默认起始页为1
    private int mLastPage = 1;
    private int mStart = 0;
    private int mLastStart = 0;
    private boolean mHasMore;
    private boolean mIsBusy = false;

    /**
     * adapter的数据源
     */
    private List<T> mDataList;

    public ListPageInfo(int numPerPage) {
        mNumPerPage = numPerPage;
    }

    private void addMore(List<T> dataList) {
        if (dataList == null) {
            return;
        }
        if (mPage == 1 || mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.addAll(dataList);
    }

    public void updateListInfo(List<T> dataList) {
        addMore(dataList);
        mIsBusy = false;
    }

    public void updateListInfo(List<T> dataList, int total) {
        addMore(dataList);
        mHasMore = mDataList.size() < total;
        mIsBusy = false;
    }

    public void updateListInfo(List<T> dataList, boolean hasMore) {
        addMore(dataList);
        mHasMore = hasMore;
        mIsBusy = false;
    }

    public void rollbackOnFail() {
        mIsBusy = false;
        mPage = mLastPage;
//        mStart = mLastStart;
    }

    /**
     * 锁定操作
     *
     * @return
     */
    public boolean tryEnterLock() {
        if (mIsBusy) {
            return false;
        }
        mIsBusy = true;
        return true;
    }

//    public int getStart() {
//        return mStart;
//    }

    public int getNumPerPage() {
        return mNumPerPage;
    }

//    public int getPage() {
//        return mStart / mNumPerPage;
//    }
//
//    public void goToHead() {
//        mStart = 0;
//    }
    public int getPage() {
        return mPage;
    }

    public void goToHead() {
        mPage = 1;
    }

    public T getItem(int position) {
        if (mDataList == null || position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    public boolean isEmpty() {
        return mDataList == null || mDataList.size() == 0;
    }

    public boolean prepareForNextPage() {
        if (hasMore()) {
//            mLastStart = mStart;
//            mStart += mNumPerPage;
            mLastPage = mPage;
            mPage++;
            return true;
        }
        return false;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public int getListLength() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    public T firstItem() {
        if (mDataList == null || mDataList.size() == 0) {
            return null;
        }
        return mDataList.get(0);
    }

    public T lastItem() {
        if (mDataList == null || mDataList.size() == 0) {
            return null;
        }
        return mDataList.get(mDataList.size() - 1);
    }

    public boolean hasMore() {
        return mDataList == null || mHasMore;
    }

    public boolean isFirstPage() {
//        return 0 == mStart;
        return mPage == 1;
    }

}