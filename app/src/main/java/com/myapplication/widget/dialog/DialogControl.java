package com.myapplication.widget.dialog;

import android.app.Dialog;

public interface DialogControl {

    void hideProgressDialog();

    Dialog showProgressDialog();

    Dialog showProgressDialog(int resId);

    Dialog showProgressDialog(String text);
}
