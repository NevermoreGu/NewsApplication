package com.myapplication.util.volleyUtils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.myapplication.base.BaseApplication;

public class TextUtil {

    public static void addTextTouchColor(final TextView textView, final int downColor, final int upColor) {
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    textView.setBackgroundColor(BaseApplication.getInstance().getResources().getColor(
                            downColor));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                    textView.setBackgroundColor(BaseApplication.getInstance().getResources().getColor(
                            upColor));
                }
                return false;
            }
        });
    }
}
