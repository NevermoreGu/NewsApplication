package com.myapplication.model;

import com.myapplication.api.MyApi;
import com.myapplication.bean.NewArticleListDao;
import com.myapplication.bean.NewsList;
import com.myapplication.event.EventCenter;
import com.myapplication.event.ListAndHeadDataEvent;
import com.myapplication.model.list.ListPageInfo;
import com.myapplication.model.list.PageMoreListDataModel;
import com.myapplication.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class NewsHeadListDataModel extends PageMoreListDataModel<NewsList, NewArticleListDao> {

    private List<NewsList> newsLists;
    private int pageSize = 0;

    public NewsHeadListDataModel(int numPerPage, String urlPart) {
        mListPageInfo = new ListPageInfo<>(numPerPage);
        mUrlPart = urlPart;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return "";
    }

    @Override
    protected void onRequestResponse(String response) {
        headData = parseData(response);
        headListData = parseList();

        boolean mHasMore = true;
        if (headListData != null && headListData.size() < pageSize) {
            mHasMore = false;
        }
        setRequestResult(headListData, mHasMore);
        ListAndHeadDataEvent<NewsList, NewArticleListDao> event = new ListAndHeadDataEvent();
        event.hasMore = mHasMore;
        event.listData = headListData;
        event.data = headData;
        event.listPageInfo = mListPageInfo;
        event.url = mUrlPart;
        event.isError = false;
        EventCenter.getInstance().post(event);
    }

    @Override
    protected void onRequestError() {
        setRequestFail();
        ListAndHeadDataEvent<NewsList, NewArticleListDao> event = new ListAndHeadDataEvent();
        event.listPageInfo = mListPageInfo;
        event.url = mUrlPart;
        event.isError = true;
        EventCenter.getInstance().post(event);
    }

    @Override
    protected void doQueryData() {
        try {
            MyApi.newsList(mUrlPart, mListPageInfo.getPage(), listener, errorListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected NewArticleListDao parseData(String is) {
        NewArticleListDao newArticleListDao = null;
        try {
            newArticleListDao = GsonUtil.toBean(is, NewArticleListDao.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newArticleListDao != null) {
            newsLists = new ArrayList<>();
            if (newArticleListDao.getNormal() != null) {
                newsLists.addAll(newArticleListDao.getNormal());
            }

            if (newArticleListDao.getOrders() != null) {
                List<NewsList> orders = newArticleListDao.getOrders();
                pageSize = mListPageInfo.getNumPerPage() - orders.size();
            }
        }
        return newArticleListDao;
    }

    @Override
    protected List<NewsList> parseList() {
        return newsLists;
    }
}
