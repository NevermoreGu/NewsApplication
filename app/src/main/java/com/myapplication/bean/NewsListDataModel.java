package com.myapplication.bean;

import com.myapplication.api.MyApi;
import com.myapplication.ui.list.ListPageInfo;
import com.myapplication.ui.list.PagedListDataModel;

import java.util.ArrayList;

public class NewsListDataModel extends PagedListDataModel<NewsList> {

    public NewsListDataModel(int numPerPage, String urlPart) {
        mListPageInfo = new ListPageInfo<>(numPerPage);
        mUrlPart = urlPart;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return "";
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
    protected ArrayList<NewsList> parseListData(String is){
        return super.parseListData(is);
    }
}
