package com.myapplication.ui.refreshmore;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.myapplication.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class CarRefreshHeaderView extends FrameLayout implements PtrUIHandler {

    private ImageView imageView;
    private AnimationDrawable anim;

    public CarRefreshHeaderView(Context context) {
        super(context);
        initViews(null);
    }

    public CarRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public CarRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.view_car_refresh_header, this);
        imageView = (ImageView) header.findViewById(R.id.img_car_refresh);
        imageView.setBackgroundResource(R.drawable.animation_car);
        anim = (AnimationDrawable) imageView.getBackground();
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        anim.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        anim.stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
