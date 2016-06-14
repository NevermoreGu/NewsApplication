package com.myapplication.activity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.myapplication.R;
import com.myapplication.base.BaseWebViewActivity;

import butterknife.InjectView;

public class NewsDetailActivity extends BaseWebViewActivity {

    @InjectView(R.id.btn_news_detail_share)
    Button btnShare;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initView() {
        super.initView();
        btnShare.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_news_detail_share:

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
