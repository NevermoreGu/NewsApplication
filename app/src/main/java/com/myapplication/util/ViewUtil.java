package com.myapplication.util;

import android.view.MotionEvent;
import android.view.View;

import com.myapplication.base.BaseApplication;

public class ViewUtil {

    public static void addTouchColor(final View view, final int downColor, final int upColor) {

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setBackgroundColor(BaseApplication.getInstance().getResources().getColor(
                            downColor));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                    view.setBackgroundColor(BaseApplication.getInstance().getResources().getColor(
                            upColor));
                }
                return false;
            }
        });

    }
}
