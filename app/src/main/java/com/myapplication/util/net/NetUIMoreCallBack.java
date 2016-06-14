package com.myapplication.util.net;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.cpsdna.mirror.ui.widget.HERecyclerView;
import com.google.gson.Gson;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by W.
 */
public abstract class NetUIMoreCallBack <T> extends NetCallBack<T>  {

    HERecyclerView heRecyclerView;
    Gson gson;
    Handler handler;

    public NetUIMoreCallBack(HERecyclerView heRecyclerView){
        this.heRecyclerView = heRecyclerView;
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onStart() {
        super.onStart();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(heRecyclerView.isFirstPage()){
                    heRecyclerView.getRecyclerView().loadComplete(true,heRecyclerView.isHasMore());
                    heRecyclerView.getProgressBar().setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onResponse(final T response) {

        try {
            JSONObject result;
            if(response instanceof String){
                result = new JSONObject((String)response);
            }else{
                String object = gson.toJson(response);
                result = new JSONObject(object);
            }

            JSONObject detail = result.getJSONObject("detail");
            int pages = detail.optInt("pages",1);
            heRecyclerView.setPages(pages);
            final int resultcode = result.optInt("result");
            final String resultNote = result.getString("resultNote");
            if(resultcode == 0){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiSucess(response);
                        if(resultcode == 0){
                            heRecyclerView.getRecyclerView().loadComplete(true,heRecyclerView.isHasMore());
                        }else{
                            heRecyclerView.getRecyclerView().loadComplete(false,heRecyclerView.isHasMore());
                        }
                    }
                });
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiError();
                        heRecyclerView.setErrortxt(resultNote);
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFauile(final Exception e) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(heRecyclerView.isFirstPage()){
                    String error = "请求异常";
                    if (e instanceof ConnectException) {// 不能在指定的ip和端口上建立连接
                        error = "无法和服务器建立连接";
                    } else if (e instanceof ConnectTimeoutException) {// 建立连接超时
                        error = "与服务器建立连接超时";
                    } else if (e instanceof SocketTimeoutException) {// 读取数据超时
                        error = "服务器读取数据超时";
                    } else if (e instanceof UnknownHostException) {
                        error = "网络异常";
                    } else if( e instanceof InterruptedIOException){
                        error = "网络连接失败";
                    } else if(e instanceof IOException){
                        if(e.getLocalizedMessage().equals("Socket Closed")){
                            return;
                        }
                        error = "网络连接中断";
                    }
                    heRecyclerView.setErrortxt(error);

                }else{
                    heRecyclerView.getRecyclerView().loadComplete(false,heRecyclerView.isHasMore());
                    heRecyclerView.getRecyclerView().getFootView().setLoadFailureStatus();
                }

            }
        });
    }


    @Override
    public void onFinish() {
        super.onFinish();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(heRecyclerView.isFirstPage()){
                    heRecyclerView.getProgressBar().setVisibility(View.GONE);
                }
            }
        });
    }

    public abstract void uiSucess(T response);
    public void uiError(){

    }
}
