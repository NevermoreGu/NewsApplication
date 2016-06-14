package com.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.myapplication.R;
import com.myapplication.bean.ImageInfo;

import java.util.List;

public class PictureBrowsePagerAdapter extends PagerAdapter {

    private List<ImageInfo> imageUrls;
    private Context mContext;

    public PictureBrowsePagerAdapter(List<ImageInfo> imageUrls, Context context) {
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

        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        Glide.with(mContext)
                .load(imageUrls.get(position).path)
                .centerCrop()
                .placeholder(R.drawable.default_img_bg)
                .crossFade()
                .into(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
