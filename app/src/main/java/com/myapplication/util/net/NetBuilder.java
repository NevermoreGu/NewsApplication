package com.myapplication.util.net;


public class NetBuilder {

    public String url;
    public String param;
    public NetCallBack callBack;

    private NetBuilder(Builder builder) {
        this.url = builder.url;
        this.param = builder.param;
        this.callBack = builder.callBack;
    }

    public static class Builder{
        private String url;
        private String param;
        private NetCallBack callBack;

        public Builder url(String url) {
            if (url == null) throw new IllegalArgumentException("url == null");
            this.url = url;
            return this;
        }

        public Builder param(String param) {
            this.param = param;
            return this;
        }

        public Builder callback(NetCallBack callBack) {
            this.callBack = callBack;
            return this;
        }

        public NetBuilder build() {
            return new NetBuilder(this);
        }
    }
}
