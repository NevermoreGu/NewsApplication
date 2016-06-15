package com.myapplication.event;


import com.myapplication.model.list.ListPageInfo;

public class ListAndHeadErrorDataEvent<T1,T2> {

    public String url;
    public ListPageInfo<T1> listPageInfo;
}
