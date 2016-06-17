package com.myapplication.model;

/**
 * Created by Administrator on 2016/6/16.
 */
public interface DataHandler<T> {

    void onDataLoaded(T listPageInfo);
}
