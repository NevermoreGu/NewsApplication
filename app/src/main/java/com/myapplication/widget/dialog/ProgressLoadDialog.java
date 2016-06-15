package com.myapplication.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.myapplication.R;

public class ProgressLoadDialog extends Dialog implements DialogControl{

    private TextView _messageTv;

    public ProgressLoadDialog(Context context) {
        super(context);
        init(context);
    }

    public ProgressLoadDialog(Context context, int defStyle) {
        super(context, defStyle);
        init(context);
    }

    protected ProgressLoadDialog(Context context, boolean cancelable,
                                 OnCancelListener listener) {
        super(context, cancelable, listener);
        init(context);
    }

    private void init(Context context) {
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_progress, null);
        _messageTv = (TextView) view.findViewById(R.id.waiting_tv);
        setContentView(view);
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        if (TDevice.isTablet()) {
//            int i = (int) TDevice.dpToPixel(360F);
//            if (i < TDevice.getScreenWidth()) {
//                WindowManager.LayoutParams params = getWindow().getAttributes();
//                params.width = i;
//                getWindow().setAttributes(params);
//            }
//        }
    }

    public void setMessage(int message) {
        _messageTv.setText(message);
    }

    public void setMessage(String message) {
        _messageTv.setText(message);
    }

    public void hideMessage() {
        _messageTv.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public Dialog showProgressDialog(Context context) {

        return null;
    }

    @Override
    public Dialog showProgressDialog(Context context,int resId) {
        return null;
    }

    @Override
    public Dialog showProgressDialog(Context context,String text) {
        return null;
    }
}