package com.pcs.hackathonandroid.interfaces;

import com.pcs.hackathonandroid.model.Register;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("user/save")
    Observable<Register> Register(@Field("full_name") String fullName, @Field("email") String email, @Field("user_id") String Uid);

}
