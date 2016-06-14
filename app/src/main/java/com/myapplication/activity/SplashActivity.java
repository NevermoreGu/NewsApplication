package com.myapplication.activity;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.myapplication.R;
import com.myapplication.api.MyApi;
import com.myapplication.base.BaseActivity;
import com.myapplication.base.BaseApplication;
import com.myapplication.base.Constants;
import com.myapplication.bean.AppConfig;
import com.myapplication.util.volleyUtils.StrErrListener;
import com.myapplication.util.volleyUtils.VolleyUtils;

import butterknife.InjectView;

public class SplashActivity extends BaseActivity {

    private Animation mAnimation = null;

    @InjectView(R.id.img_splash)
    ImageView imgSplash;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        GetConfig();
//        mAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_alpha);
//        imgSplash.setAnimation(mAnimation);
//        mAnimation.setAnimationListener(new AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }


    private void GetConfig() {
//        if (BaseApplication.getInstance().getRequestQueue().getCache() != null) {
//            Cache cache = BaseApplication.getInstance().getRequestQueue().getCache();
//            if (cache.get(Constants.HTTP_CONFIG) != null && cache.get(Constants.HTTP_CONFIG).data != null) {
//
//                String str = new String(cache.get(Constants.HTTP_CONFIG).data);
//
//            }
//        }

        Response.Listener<AppConfig> configListener = new Response.Listener<AppConfig>() {
            @Override
            public void onResponse(AppConfig response) {
                handlerMessage(response);
            }
        };

        try {
            MyApi.getAppConfig(configListener, new StrErrListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handlerMessage(AppConfig response) {
        if (response != null && response.getState() != null) {
            if (response.getState().equals("0")) {

            } else if (response.getState().equals("1")) {
                BaseApplication.config = response;
                Constants.HTTP_HEAD = BaseApplication.config.getApi_root();
                Constants.HTTP_HEAD_PREFIX = BaseApplication.config.getApi_prefix();
                openActivity(MainActivity.class);
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleyUtils.getInstance(this).cancelPendingRequests(Constants.APPCONFIG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
