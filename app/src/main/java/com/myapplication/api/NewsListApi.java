package com.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsListApi {

    @GET("{id}")
    Call<ResponseBody> getNewsList(@Query("key") String key, @Query("page") int page, @Query("pagesize") int pageSize, @Query("newdata") int newData);
}
