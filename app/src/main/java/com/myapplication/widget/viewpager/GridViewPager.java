package com.myapplication.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class GridViewPager extends ViewPager {

    private int touchSlop;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mLastMotionX;
    private float mLastMotionY;

    private List<GridView> mGridViewList = null;

    private int mRowNumber = 0;
    private int mColumnNumber = 0;

    private BaseAdapter mAdapter;

    private View mEmptyView = null;

    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;

    private int mSelection = -1;
    private Context context;

    public GridViewPager(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GridViewPager);
            final int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.GridViewPager_gvpColumnNumber) {
                    mColumnNumber = a.getInt(attr, -1);
                } else if (attr == R.styleable.GridViewPager_gvpRowNumber) {
                    mRowNumber = a.getInt(attr, -1);
                } else if (attr == R.styleable.GridViewPager_android_padding) {
                    int padding = a.getDimensionPixelSize(attr, 0);
                    setPadding(padding, padding, padding, padding);
                } else if (attr == R.styleable.GridViewPager_android_paddingLeft) {
                    mPaddingLeft = a.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.GridViewPager_android_paddingRight) {
                    mPaddingRight = a.getDimensionPixelSize(attr, 0);
                }
            }

            a.recycle();
        }
        init();
    }

    private void init() {

        mGridViewList = new ArrayList<>();
        //getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = ev.getX();
                mInitialMotionY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMotionX = ev.getX();
                mLastMotionY = ev.getY();
                if ((int) Math.abs(mLastMotionX - mInitialMotionX) > touchSlop || (int) Math.abs(mLastMotionY - mInitialMotionY) > touchSlop) {
                    mInitialMotionX = 0.0f;
                    mInitialMotionY = 0.0f;
                }
                break;
            case MotionEvent.ACTION_UP:

                mLastMotionX = ev.getX();
                mLastMotionY = ev.getY();
                if ((int) mInitialMotionX != 0 && (int) mInitialMotionY != 0) {
                    if ((int) Math.abs(mLastMotionX - mInitialMotionX) < touchSlop
                            && (int) Math.abs(mLastMotionY - mInitialMotionY) < touchSlop) {
                        mInitialMotionX = 0.0f;
                        mInitialMotionY = 0.0f;
                        mLastMotionX = 0.0f;
                        mLastMotionY = 0.0f;
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingRight = right;
        super.setPadding(0, top, 0, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public int getPageCount() {
        return mGridViewList.size();
    }

    public int getPageSize() {
        return mColumnNumber * mRowNumber;
    }

    public void setSelection(int position) {
        final int pageSize = getPageSize();
        if (mAdapter == null || pageSize <= 0) {
            mSelection = position;
            return;
        }
        mSelection = -1;
        setCurrentItem(position / pageSize, true);
    }

    public int getSelection() {
        return getCurrentItem() * getPageSize();
    }

    public void setEmptyView(TextView emptyView) {
        mEmptyView = emptyView;
    }

    public void setPageAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        resetAdapter();
    }

    public void notifyDataSetChanged() {
        resetAdapter();
    }

    private void resetAdapter() {
        int pageSize = mColumnNumber * mRowNumber;
        if (pageSize <= 0)
            return;

        if (mAdapter.getCount() == 0) {
            mGridViewList.removeAll(mGridViewList);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.VISIBLE);
        } else {
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        }
        int pageCount = mAdapter.getCount() / pageSize;
        if (mAdapter.getCount() % pageSize == 0) {
            pageCount--;
        }
        int listSize = mGridViewList.size() - 1;
        GridView gridview;
        GridAdapter gridAdapter;
        for (int i = 0; i <= Math.max(listSize, pageCount); i++) {
            if (i <= listSize && i <= pageCount) {
                gridview = mGridViewList.get(i);
                gridAdapter = new GridAdapter(i, pageSize, mAdapter);
                gridview.setAdapter(gridAdapter);
                mGridViewList.set(i, gridview);
                continue;
            }
            if (i > listSize && i <= pageCount) {
                gridview = new GridView(context);
                gridview.setPadding(10, 0, 10, 0);
                gridview.setNumColumns(mColumnNumber);
                gridAdapter = new GridAdapter(i, pageSize, mAdapter);
                gridview.setAdapter(gridAdapter);
                mGridViewList.add(gridview);
                continue;
            }
            if (i > pageCount && i <= listSize) {
                mGridViewList.remove(pageCount + 1);
                continue;
            }
        }
        super.setAdapter(new GridPagerAdapter());
        if (mSelection >= 0)
            setSelection(mSelection);
    }

    private class GridPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mGridViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mGridViewList.get(position), new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return mGridViewList.get(position);
        }
    }

    private class GridAdapter extends BaseAdapter {
        int mPage;
        int mSize;
        BaseAdapter mAdapter;

        public GridAdapter(int page, int size, BaseAdapter adapter) {
            mPage = page;
            mSize = size;
            mAdapter = adapter;
        }

        @Override
        public int getCount() {
            if (mAdapter.getCount() % mSize == 0)
                return mSize;
            else if (mPage < mAdapter.getCount() / mSize) {
                return mSize;
            } else {
                return mAdapter.getCount() % mSize;
            }
        }

        @Override
        public Object getItem(int position) {
            return mAdapter.getItem(mPage * mSize + position);
        }

        @Override
        public long getItemId(int position) {
            return mAdapter.getItemId(mPage * mSize + position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mAdapter.getView(mPage * mSize + position, convertView, parent);
        }

    }

}
