package com.myapplication.event;


import com.myapplication.model.list.ListPageInfo;

import java.util.List;

public class ListAndHeadDataEvent<T1,T2> {

    public boolean hasMore;
    public List<T1> listData;
    public T2 data;
    public String url;
    public ListPageInfo<T1> listPageInfo;
    public boolean isError = false;
}
