package com.myapplication.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class NavigationBar extends View {

    public NavigationBar(Context context) {
        this(context, null);
        initView();
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
        initView();
    }

    private void initView(){

    }
}
