package com.myapplication.event;

import com.myapplication.ui.list.ListPageInfo;

import java.util.List;

public class ListAndHeadErrorDataEvent<T1,T2> {

    public String url;
    public ListPageInfo<T1> listPageInfo;
}
