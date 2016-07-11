package com.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.myapplication.R;
import com.myapplication.adapter.PictureBrowsePagerAdapter;
import com.myapplication.base.BaseActivity;
import com.myapplication.bean.ImageInfo;
import com.myapplication.widget.viewpager.autoscrollviewpager.AutoScrollViewPager;

import java.util.ArrayList;

import butterknife.InjectView;

public class PicturesBrowseActivity extends BaseActivity {

    @InjectView(R.id.vp_picture_browse)
    AutoScrollViewPager vpBrowse;

    private int position = 0;
    private ArrayList<ImageInfo> imageInfos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture_browse;
    }

    @Override
    public void initView() {
        vpBrowse.setAdapter(new PictureBrowsePagerAdapter(imageInfos, this));
        vpBrowse.startAutoScroll(0);
        vpBrowse.setCurrentItem(position - 1);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position");
        imageInfos = bundle.getParcelableArrayList("pictures");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
