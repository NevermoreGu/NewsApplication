package com.myapplication.ui.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myapplication.R;

public class LoadMoreDefaultFooterView extends RelativeLayout implements LoadMoreUIHandler {

    private TextView mTextView;
    private ProgressBar mProgressBar;

    public LoadMoreDefaultFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.list_cell_footer, this);
        mTextView = (TextView) findViewById(R.id.text);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void onLoading(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mTextView.setText(R.string.loading);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (!hasMore) {
            setVisibility(VISIBLE);
            if (empty) {
                mProgressBar.setVisibility(View.GONE);
                mTextView.setText(R.string.loading_no_more);
            } else {
                mProgressBar.setVisibility(View.GONE);
                mTextView.setText(R.string.loading_no_more);
            }
        } else {
            setVisibility(GONE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mTextView.setText(R.string.loading);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        mTextView.setText(R.string.error_view_load_error_click_to_refresh);
    }
}

