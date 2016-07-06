package com.myapplication.api;

import com.android.volley.Request;
import com.android.volley.Response;
import com.myapplication.base.BaseApplication;
import com.myapplication.base.Constants;
import com.myapplication.bean.AppConfig;
import com.myapplication.util.net.NetBuilder;
import com.myapplication.util.net.NetUICallBack;
import com.myapplication.util.net.VolleyNet;
import com.myapplication.util.volleyUtils.StrErrListener;
import com.myapplication.util.volleyUtils.StringRequestGet;
import com.myapplication.util.volleyUtils.VolleyUtils;
import com.myapplication.util.volleyUtils.XmlRequestGet;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MyApi {

    public static void getAppConfig(Response.Listener<AppConfig> listener,
                                    StrErrListener errorListener) throws Exception {
        String url = Constants.HTTP_CONFIG;
        Request xmlRequest = new XmlRequestGet(url, AppConfig.class, listener, errorListener);
        xmlRequest.setShouldCache(true);
        VolleyUtils.getInstance(BaseApplication.getInstance()).addToRequestQueue(xmlRequest,
                Constants.APPCONFIG);
    }

    public static void car(int pageIndex, Response.Listener<String> listener, Response.ErrorListener errorListener)
            throws Exception {
        String url = "http://open.tmtsp.com/plugin/sjfw-api/services?brange=118.774389,31.992539&distance=0&stype=&scredit=0&sprice=0&page=" + pageIndex + "&pagesize=8";
        StringRequestGet requestGet = new StringRequestGet(url, listener, errorListener);
        VolleyUtils.getInstance(BaseApplication.getInstance()).addToRequestQueue(requestGet,
                Constants.CAR);
    }

    public static void newsList(String urlPart, String id, int pageIndex, Response.Listener<String> listener, Response.ErrorListener errorListener)
            throws Exception {
        String url = urlPart + "?key=" + id + "&page=" + pageIndex + "&pagesize=" + Constants.PAGE_SIZE + "&newdata=1";
        StringRequestGet requestGet = new StringRequestGet(url, listener, errorListener);
        VolleyUtils.getInstance(BaseApplication.getInstance()).addToRequestQueue(requestGet,
                Constants.NEWSLIST);
    }

    public static void newsList(String urlPart, int pageIndex, Response.Listener<String> listener, Response.ErrorListener errorListener)
            throws Exception {
        String url = urlPart + "&page=" + pageIndex + "&pagesize=" + Constants.PAGE_SIZE + "&newdata=1";
        StringRequestGet requestGet = new StringRequestGet(url, listener, errorListener);
        VolleyUtils.getInstance(BaseApplication.getInstance()).addToRequestQueue(requestGet,
                Constants.NEWSLIST);
    }

    public static void newsList(String urlPart, String id, int pageIndex, Callback<ResponseBody> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlPart)
                .build();
        NewsListApi newsListApi = retrofit.create(NewsListApi.class);
        Call<ResponseBody> call = newsListApi.getNewsList(id, pageIndex, Constants.PAGE_SIZE, 1);
        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
        call.enqueue(callback);
    }

    public static void newsList(String urlPart, int pageIndex, NetUICallBack callBack)
            throws Exception {
        String url = urlPart + "&page=" + pageIndex + "&pagesize=" + Constants.PAGE_SIZE + "&newdata=1";
        NetBuilder builder = new NetBuilder.Builder().url(url).callback(callBack).build();
        VolleyNet.getInstance(BaseApplication.getInstance()).addToRequestQueue(Constants.NEWSLIST, builder);
    }

    public static void newsScroll(int pageIndex, Response.Listener<String> listener, Response.ErrorListener errorListener)
            throws Exception {
        String url = " http://open.tmtsp.com/app/article/list?type=image&key=55c0680627f38e1c0700021f&pagesize=4";
        StringRequestGet requestGet = new StringRequestGet(url, listener, errorListener);
        VolleyUtils.getInstance(BaseApplication.getInstance()).addToRequestQueue(requestGet,
                Constants.NEWSSCROLL);
    }

    //苏轼   水调歌头
    //明月几时有，把酒问青天，不知天上宫阙，今夕是何年。我欲乘风归去，又恐琼楼玉宇，高处不胜寒，起舞弄清影，何似在人间。
    // 转朱阁，低绮户，照无眠，不应有恨，何事长向别时圆，人有悲欢离合，月有阴晴圆缺，此事古难全，但愿人长久，千里共婵娟，

    //苏轼 江城子
    //十年生死两茫茫，不思量，自难忘。千里孤坟，无处话凄凉，纵使相逢应不识，尘满面，鬓如霜。
    //夜来幽梦忽还乡，小轩窗，正梳妆。相顾无言，惟有泪千行。料得年年肠断处，明月夜，短松冈。

    /**
     * videos list
     * @param urlPart
     * @param id
     * @param pageIndex
     * @param listener
     * @param errorListener
     * @throws Exception
     */
    public static void videosList(String urlPart, String id, int pageIndex, Response.Listener<String> listener, Response.ErrorListener errorListener)
            throws Exception {
//        String url = urlPart + "?key=" + id + "&page=" + pageIndex + "&pagesize=" + Constants.PAGE_SIZE + "&newdata=1";
        String url = "http://open.tmtsp.com/app/video/list?key=566100f727f38edc0500002a&page=1&pagesize=12";
        StringRequestGet requestGet = new StringRequestGet(url, listener, errorListener);
        VolleyUtils.getInstance(BaseApplication.getInstance()).addToRequestQueue(requestGet,
                Constants.VIDEOSLIST);
    }

    public static String firstPageData4laso(String vspId, String authId, String operUserId) {
        JSONObject root = new JSONObject();
        JSONObject params = new JSONObject();
        try {
            root.put("cmd", "rightUpperData4laso");
            root.put("params", params);

            params.put("vspId", vspId);
            params.put("authId", authId);
            params.put("operUserId", operUserId);
            return root.toString();
        } catch (JSONException e) {

            e.printStackTrace();
            return null;
        }
    }
}
