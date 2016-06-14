package com.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {

    private ViewPager viewPager;

    private int[] mIds = new int[]{
            R.mipmap.guide_image1, R.mipmap.guide_image2, R.mipmap.guide_image3
    };

    private List<ImageView> imageViews = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        viewPager = (ViewPager) findViewById(R.id.vp_test);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(TestActivity.this);
                imageView.setImageResource(mIds[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView);
                imageViews.add(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return mIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}
