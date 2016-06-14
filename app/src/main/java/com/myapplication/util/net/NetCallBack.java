package com.myapplication.util.net;

public abstract class NetCallBack<T> {

    public void onStart() {}

    public abstract void onResponse(T response);

    public abstract void onErrorResponse(Exception e);

    public void onFinish() {
    }
}
