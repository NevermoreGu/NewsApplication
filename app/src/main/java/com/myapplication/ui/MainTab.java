package com.myapplication.ui;


import com.myapplication.Fragment.ExploreFragment;
import com.myapplication.Fragment.MyInformationFragment;
import com.myapplication.Fragment.NewsFragment;
import com.myapplication.Fragment.VideoFragment;
import com.myapplication.R;

public enum MainTab {

    NEWS(0, R.string.main_tab_news, R.drawable.tab_icon_news,
            NewsFragment.class),

    TWEET(1, R.string.main_tab_video, R.drawable.tab_icon_video,
            VideoFragment.class),

    EXPLORE(2, R.string.main_tab_explore, R.drawable.tab_icon_explore,
            ExploreFragment.class),

    ME(3, R.string.main_tab_my, R.drawable.tab_icon_my,
            MyInformationFragment.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    MainTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
