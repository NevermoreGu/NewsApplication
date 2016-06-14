package com.myapplication.util.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.myapplication.widget.dialog.WaitDialog;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public abstract class NetUICallBack<T> extends NetCallBack<T> {

    Context mContext;
    Handler mHandler;
    ProgressDialog progressDialog;
    boolean showProgress = true;
    private WaitDialog waitDialog;

    public NetUICallBack(Context context) {
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
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
//            progressDialog = ProgressDialog.show(mContext,null, null ,
//                    false,false);
            if (waitDialog == null) {
//                waitDialog = new WaitDialog(mContext, "加载中...");
            }
            if (waitDialog != null && !waitDialog.isShowing()) {
                waitDialog.show();
            }
        }

    }

    @Override
    public void onResponse(final T response) {
        if (response instanceof String) {
            String js = (String) response;
            try {
                JSONObject json = new JSONObject(js);
                int result = json.getInt("result");
                if (result == 0) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            uiSucess(response);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            uiError(response);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            BaseBean bean = (BaseBean) response;
            if (bean instanceof AvailableObjListBean) {
                if (bean.result == 0 || bean.result == 11) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            uiSucess(response);
                        }
                    });
                }
                return;
            }
            if (bean.result == 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiSucess(response);
                    }
                });
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiError(response);
                    }
                });
            }

        }

    }

    @Override
    public void onFauile(final Exception e) {
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

                uiFauile(error);
            }
        });
    }

    @Override
    public void onFinish() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                uiFinish();
            }
        });
    }

    public abstract void uiSucess(T response);

    public void uiError(T response) {
        final BaseBean bean = (BaseBean) response;
        if (!TextUtils.isEmpty(bean.resultNote)) {
            Toast.makeText(mContext, bean.resultNote, Toast.LENGTH_LONG).show();
        }
    }

    public void uiFauile(String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
    }

    public void uiFinish() {
    }
}
