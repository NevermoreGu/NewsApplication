package com.myapplication.model.list;

import java.util.List;

public abstract class PagedListAndHeadDataModel<T1,T2> extends PagedListDataModel<T1> {

    protected T2 headData;
    protected List<T1> headListData;

//    @Override
//    protected void onRequestFinish(String response) {
//        try {
//            headData = parseData(response);
//            headListData = parseList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            headListData = new ArrayList<>();
//        }
//        boolean mHasMore = true;
//        if (headListData.size() < mListPageInfo.getNumPerPage()) {
//            mHasMore = false;
//        }
//        ListAndHeadDataEvent<T1,T2> event = new ListAndHeadDataEvent();
//        event.hasMore = true;
//        event.listData = headListData;
//        event.data = headData;
//        setRequestResult(headListData, true);
//        EventCenter.getInstance().post(event);
//    }
//
//    @Override
//    protected void onRequestFail() {
//        super.onRequestFail();
//    }

    protected T2 parseData(String is){
        return null;
    }

    protected List<T1> parseList(){
        return null;
    }

}
