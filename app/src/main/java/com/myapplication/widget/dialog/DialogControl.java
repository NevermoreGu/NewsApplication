package com.myapplication.widget.dialog;

import android.app.Dialog;
import android.content.Context;

public interface DialogControl {

    void hideProgressDialog();

    Dialog showProgressDialog(Context context);

    Dialog showProgressDialog(Context context,int resId);

    Dialog showProgressDialog(Context context,String text);
}
