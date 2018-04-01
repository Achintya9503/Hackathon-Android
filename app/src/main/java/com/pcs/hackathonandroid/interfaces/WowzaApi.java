package com.pcs.hackathonandroid.interfaces;

import com.pcs.hackathonandroid.beans.wowza.LiveStream;
import com.pcs.hackathonandroid.beans.wowza.WowzaSCResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WowzaApi {

    @Headers({
            "Content-Type:application/json",
            "wsc-api-key: [key]",
            "wsc-access-key: [key]"
    })
    @POST("api/v1.1/live_streams")
    Observable<WowzaSCResponse> generateLiveStream(@Body LiveStream liveStream);
}
