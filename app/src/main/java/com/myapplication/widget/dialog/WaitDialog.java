package com.myapplication.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapplication.R;


public class WaitDialog extends Dialog implements DialogControl{

    public WaitDialog(Context context) {
        super(context);
    }

    public WaitDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        /**
         * anim放在drawable目录下，而不是anim目录下
         */
        ImageView imageView = (ImageView) findViewById(R.id.img_loading_progress);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getBackground();
        animationDrawable.start();
    }


    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            TextView txt = (TextView) findViewById(R.id.tv_loading_progress);
            txt.setVisibility(View.VISIBLE);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public static WaitDialog createProgressDialog(Context context,
                                                  CharSequence message) {
        WaitDialog waitDialog = new WaitDialog(context,
                R.style.style_loading_progress);
        waitDialog.setContentView(R.layout.dialog_wait);
        TextView txt = (TextView) waitDialog
                .findViewById(R.id.tv_loading_progress);
        if (message == null || message.length() == 0) {
            txt.setVisibility(View.GONE);
        } else {
            txt.setText(message);
        }

        // 设置点击其他地方不会消失
        waitDialog.setCancelable(false);

        waitDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = waitDialog.getWindow().getAttributes();
        lp.dimAmount = 0.3f;
        waitDialog.getWindow().setAttributes(lp);
        return waitDialog;
    }

    public static void showProgressDialog(WaitDialog waitDialog) {
        waitDialog.show();
    }

    public static void cancelProgressDialog(WaitDialog waitDialog) {
        waitDialog.dismiss();
    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public Dialog showProgressDialog() {

        return null;
    }

    @Override
    public Dialog showProgressDialog(int resId) {
        return null;
    }

    @Override
    public Dialog showProgressDialog(String text) {
        return null;
    }
}
