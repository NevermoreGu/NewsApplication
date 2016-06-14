package com.myapplication.interf;

import android.app.ProgressDialog;

public interface DialogControl {

	void hideWaitDialog();

	ProgressDialog showWaitDialog();

	ProgressDialog showWaitDialog(int resId);

	ProgressDialog showWaitDialog(String text);
}
