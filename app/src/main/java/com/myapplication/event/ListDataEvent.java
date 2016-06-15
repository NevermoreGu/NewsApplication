package com.myapplication.event;


import com.myapplication.model.list.ListPageInfo;

import java.util.List;

public class ListDataEvent<T> {

    public boolean hasMore;
    public List<T> newsList;
    public String url;
    public ListPageInfo<T> listPageInfo;
    public boolean isError = false;
}
