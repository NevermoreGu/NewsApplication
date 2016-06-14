package com.myapplication.base;

import android.view.View;
import android.webkit.WebView;

import com.myapplication.R;
import com.myapplication.util.WebHelper;

import butterknife.InjectView;

public abstract class BaseWebViewActivity extends BaseActivity {

    @InjectView(R.id.webView)
    protected WebView mWebView;

    @Override
    public void initView() {
        WebHelper.initWebView(mWebView);
    }

    protected void recycleWebView() {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onDestroy() {
        recycleWebView();
        super.onDestroy();
    }
}
