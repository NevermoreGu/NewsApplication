package com.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class NewsAutoScrollPagerAdapter extends PagerAdapter {

    private List<String> imageUrls;
    private Context mContext;

    public NewsAutoScrollPagerAdapter(List<String> imageUrls, Context context) {
        this.mContext = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        if (imageUrls != null) {
            return imageUrls.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        SimpleDraweeView draweeView = new SimpleDraweeView(mContext);
        draweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        draweeView.setAspectRatio(2f);
        container.addView(draweeView);
        Uri uri = Uri.parse(imageUrls.get(position));
        draweeView.setImageURI(uri);
        return draweeView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
