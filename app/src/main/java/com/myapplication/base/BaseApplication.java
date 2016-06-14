package com.myapplication.base;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.myapplication.bean.AppConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class BaseApplication extends Application {

    public static BaseApplication instance;
    public static AppConfig config = null;

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        config = new AppConfig();
        Fresco.initialize(this);
        refWatcher = LeakCanary.install(this);
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
