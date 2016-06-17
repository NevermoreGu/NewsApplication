package com.myapplication.util.net;

public abstract class NetCallBack {

    public void onStart() {}

    public abstract void onResponse(String response);

    public abstract void onErrorResponse(Exception e);

    public void onFinish() {}
}
