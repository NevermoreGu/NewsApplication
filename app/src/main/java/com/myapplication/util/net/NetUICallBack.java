package com.myapplication.util.net;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public abstract class NetUICallBack extends NetCallBack {

    @Override
    public void onStart() {}

    @Override
    public void onResponse(String response) {}

    @Override
    public void onErrorResponse(Exception e) {

        String error = null;

        if (e instanceof ConnectException) {// 不能在指定的ip和端口上建立连接
            error = "无法和服务器建立连接";
        } else if (e instanceof ConnectTimeoutException) {// 建立连接超时
            error = "与服务器建立连接超时";
        } else if (e instanceof SocketTimeoutException) {// 读取数据超时
            error = "服务器读取数据超时";
        } else if (e instanceof UnknownHostException) {
            error = "网络异常";
        } else if (e instanceof InterruptedIOException) {
            error = "网络连接失败";
        } else if (e instanceof IOException) {
            if (e.getLocalizedMessage().equals("Socket Closed")) {
                return;
            }
            error = "网络连接异常";
        }

        uiErrorResponse(error);

    }

    @Override
    public void onFinish() {}

    public void uiErrorResponse(String error) {
    }

}
