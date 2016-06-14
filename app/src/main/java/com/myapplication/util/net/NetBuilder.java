package com.myapplication.util.net;


import com.myapplication.bean.Entity;

public class NetBuilder<T extends Entity> {

    public String url;
    public String param;
    public Class<T> clazz;
    public NetCallBack<T> callBack;

    private NetBuilder(Builder builder) {
        this.url = builder.url;
        this.param = builder.param;
        this.clazz = builder.clazz;
        this.callBack = builder.callBack;
    }

    public static class Builder<T>{
        private String url;
        private String param;
        private Class<T> clazz;
        private NetCallBack<T> callBack;

        public Builder url(String url){
            if (url == null) throw new IllegalArgumentException("url == null");
            this.url = url;
            return this;
        }

        public Builder param(String param){
            this.param = param;
            return this;
        }

        public Builder clazz(Class<T> cls){
            this.clazz = cls;
            return this;
        }

        public Builder callback(NetCallBack<T> callBack){
            this.callBack = callBack;
            return  this;
        }

        public NetBuilder build(){
            return new NetBuilder(this);
        }
    }
}
