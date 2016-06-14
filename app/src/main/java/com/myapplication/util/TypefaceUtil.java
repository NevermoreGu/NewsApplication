package com.myapplication.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import com.myapplication.base.BaseApplication;

public class TypefaceUtil {

    private static Typeface getTypeface() {
        Context context = BaseApplication.getInstance();

        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "DS-DIGI.TTF");
        return font;
    }

    public static void setTypefaceWithColor(TextView tv, int textId,
                                            String colorStr) {
        try {
            int color = Color.parseColor(colorStr);
            tv.setTextColor(color);
        } catch (Exception e) {
        }
        setTypeface(tv, textId);
    }

    public static void setTypefaceWithColor(TextView tv, int textId, int colorId) {
        tv.setTextColor(BaseApplication.getInstance().getResources()
                .getColor(colorId));
        setTypeface(tv, textId);
    }

    public static void setTypefaceWithColor(TextView tv, int colorId) {
        tv.setTextColor(BaseApplication.getInstance().getResources()
                .getColor(colorId));
        setTypeface(tv);
    }

    public static void setTypefaceWithColor(TextView tv, String colorStr) {
        try {
            int color = Color.parseColor(colorStr);
            tv.setTextColor(color);
        } catch (Exception e) {
        }

        setTypeface(tv);
    }

    public static void setTypeface(TextView tv, int textId) {
        setTypeface(tv, BaseApplication.getInstance().getString(textId));
    }

    public static void setTypeface(TextView tv, String text) {
        if (StringUtils.isEmpty(text))
            return;
        tv.setText(text);
        setTypeface(tv);
    }

    public static void setTypeFaceWithText(TextView tv, int faRes, String text) {
        String lastText = BaseApplication.getInstance().getResources().getString(faRes) + " " + text;
        setTypeface(tv, lastText);
    }

    public static void setTypeface(TextView tv) {
        tv.setTypeface(getTypeface());
    }
}
