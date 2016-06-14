package com.myapplication.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	public static void toast(Context context, int textId) {
		toast(context, context.getResources().getString(textId));
	}

	public static void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
