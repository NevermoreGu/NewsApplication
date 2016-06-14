package com.myapplication.util.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.myapplication.widget.dialog.DialogControl;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public abstract class NetUICallBack<T> extends NetCallBack<T> {

    private Context mContext;
    private Handler mHandler;
    private boolean showProgress = true;
    private DialogControl progress;


    public NetUICallBack(Context context, DialogControl progress) {
        this.mContext = context;
        this.progress = progress;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public NetUICallBack hide() {
        this.showProgress = false;
        return this;
    }

    public NetUICallBack show() {
        this.showProgress = true;
        return this;
    }

    @Override
    public void onStart() {
        if (showProgress) {
            progress.showProgressDialog();
        }
    }

    @Override
    public void onResponse(final T response) {
    }

    @Override
    public void onErrorResponse(final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                String error = "请求异常";

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
                    error = "网络连接中断";
                }

//                uiFauile(error);
            }
        });
    }

    @Override
    public void onFinish() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
//        if (waitDialog != null) {
//            waitDialog.dismiss();
//            waitDialog = null;
//        }
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                uiFinish();
//            }
//        });
    }

//    public abstract void uiSucess(T response);
//
//    public void uiError(T response) {
//        final BaseBean bean = (BaseBean) response;
//        if (!TextUtils.isEmpty(bean.resultNote)) {
//            Toast.makeText(mContext, bean.resultNote, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void uiFauile(String error) {
//        Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
//    }
//
//    public void uiFinish() {
//    }
}
