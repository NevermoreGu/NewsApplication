package com.myapplication.adapter.base;

import android.util.SparseArray;
import android.view.ViewGroup;

import com.myapplication.adapter.base.entity.MultiItemEntity;

public abstract class BaseMultiItemQuickAdapter<T extends MultiItemEntity> extends BaseQuickAdapter {

    private SparseArray<Integer> layouts;

    public BaseMultiItemQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected int getDefItemViewType(int position) {
        return ((MultiItemEntity) getData().get(position)).getItemType();
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    protected void addItemType(int type, int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        convert(helper, (T) item);
    }

    protected abstract void convert(BaseViewHolder helper, T item);


}


