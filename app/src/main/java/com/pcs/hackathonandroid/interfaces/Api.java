package com.pcs.hackathonandroid.interfaces;

import com.pcs.hackathonandroid.beans.Response;
import com.pcs.hackathonandroid.beans.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("user/save")
    Observable<Response> saveUser(@Body User user);

}
