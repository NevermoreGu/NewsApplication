package com.myapplication.Fragment;

import com.google.gson.reflect.TypeToken;
import com.myapplication.adapter.FragmentVideoListAdapter;
import com.myapplication.api.MyApi;
import com.myapplication.base.BaseListAdapter;
import com.myapplication.base.BaseListFragment;
import com.myapplication.bean.VideoList;
import com.myapplication.util.GsonUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class VideoFragment extends BaseListFragment<VideoList> {


    @Override
    protected BaseListAdapter<VideoList> getListAdapter() {
        return new FragmentVideoListAdapter();
    }

    @Override
    protected String getListCacheKey() {
        return "video";
    }

    @Override
    protected List<VideoList> parseList(String string) throws Exception {
        Type type = new TypeToken<List<VideoList>>() {}.getType();
        List<VideoList> newArticleListDao = GsonUtil.toBean(string, type);
        return newArticleListDao;
    }

    @Override
    protected List<VideoList> readList(Serializable serializable) {
        return (List<VideoList>) serializable;
    }

    @Override
    protected void sendListRequestData() {
        try {
            MyApi.videosList("", "", mCurrentPage, listener, errorListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
