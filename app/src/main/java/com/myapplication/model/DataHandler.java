package com.myapplication.model;

import com.myapplication.bean.Entity;

/**
 * Created by Administrator on 2016/6/16.
 */
public interface DataHandler<T extends Entity> {

    void onDataLoaded(T data);

}
