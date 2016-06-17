package com.myapplication.model;

import com.myapplication.api.MyApi;
import com.myapplication.bean.NewsList;
import com.myapplication.model.list.ListPageInfo;
import com.myapplication.model.list.PageListDataModel;

import java.util.ArrayList;

public class NewNewsListDataModel extends PageListDataModel<NewsList> {

    public NewNewsListDataModel(int numPerPage, String urlPart) {
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
            MyApi.newsList(mUrlPart, mListPageInfo.getPage(), netUICallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ArrayList<NewsList> parseListData(String is) {
        return super.parseListData(is);
    }
}
