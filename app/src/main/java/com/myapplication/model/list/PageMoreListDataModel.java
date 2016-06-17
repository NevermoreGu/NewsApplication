package com.myapplication.model.list;

import com.myapplication.bean.Entity;

import java.util.List;

public abstract class PageMoreListDataModel<T1 extends Entity, T2 extends Entity> extends PageListDataModel<T1> {

    protected T2 headData;

    protected List<T1> headListData;

    protected T2 parseData(String is) {
        return null;
    }

    protected List<T1> parseList() {
        return null;
    }

}
