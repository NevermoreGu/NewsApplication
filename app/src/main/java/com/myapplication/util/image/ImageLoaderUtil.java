package com.myapplication.util.image;

import android.content.Context;

/**
 * Created by Administrator on 2016/7/14.
 */
public class ImageLoaderUtil {

    public static final int PIC_LARGE = 0;
    public static final int PIC_MEDIUM = 1;
    public static final int PIC_SMALL = 2;

    public static final int LOAD_STRATEGY_NORMAL = 0;
    public static final int LOAD_STRATEGY_ONLY_WIFI = 1;

    private static ImageLoaderUtil mInstance;
    private BaseImageLoaderProvider mStrategy;

    private ImageLoaderUtil() {
        mStrategy = new GlideImageLoaderProvider();
    }

    //single instance
    public static ImageLoaderUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }


    public void loadImage(Context context, ImageLoader img) {
        mStrategy.loadImage(context, img);
    }

    public void setLoadImgStrategy(BaseImageLoaderProvider strategy) {
        mStrategy = strategy;
    }
}
