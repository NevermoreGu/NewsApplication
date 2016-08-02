package com.myapplication.api;

import com.myapplication.bean.User;
import com.myapplication.bean.UserRequest;
import com.myapplication.model.BaseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("saasapi/saasapi")
    Call<BaseModel<User>> login(@Body UserRequest request);
}
