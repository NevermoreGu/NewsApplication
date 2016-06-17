package com.myapplication.model;

import com.myapplication.bean.Entity;
import com.myapplication.util.TDevice;

public abstract class BaseModel<T extends Entity> {

    protected T results;

    /**
     * 必须实现的方法，联网请求数据
     */
    protected abstract void doQueryData();

    public void queryFirstPage() {
        doQueryDataInner();
    }

    private void doQueryDataInner() {
        if (isReadCacheData()) {

        } else {
            doQueryData();
        }
    }

    /**
     * 判断是否需要读取缓存的数据
     *
     * @return
     */
    protected boolean isReadCacheData() {
        if (!TDevice.hasInternet()) {
            return true; //读取缓存
        }
        return false;  //联网请求数据
    }
}
