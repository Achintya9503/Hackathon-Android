package com.pcs.hackathonandroid.interfaces;

import com.pcs.hackathonandroid.beans.Response;
import com.pcs.hackathonandroid.beans.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @POST("user/save")
    Observable<Response> saveUser(@Body User user);

    @GET("friends")
    Observable<List<User>> getFriends(@Header("token") String token);

    @GET("user/exploreFriends")
    Observable<List<User>> exploreFriends(@Header("token") String token);

    @POST("friends/add/{friend_id}")
    Observable<Response> addFriend(@Header("token") String token, @Path("friend_id") String friendUid);

    @PUT("user/streaming/{streaming}")
    Observable<Response> streaming(@Header("token") String token, @Path("streaming") String isStreaming);
}
