package com.myapplication.ui.MediaVideo;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by admin on 2016/3/19.
 */
public class MediaVideo extends SurfaceView {

    private int videoWidth = 0;
    private int videoHeight = 0;

    private MediaPlayer mediaPlayer;

    private SurfaceHolder surfaceHolder = getHolder();

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    public MediaVideo(Context context) {
        super(context);
    }

    public MediaVideo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void initView() {

        surfaceHolder.addCallback(callback);
    }

    public void openVideo() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }


}
