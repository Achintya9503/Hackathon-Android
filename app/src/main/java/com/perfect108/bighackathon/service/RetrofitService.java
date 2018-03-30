package com.perfect108.bighackathon.service;

import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kunal on 30-03-2018.
 */

public class RetrofitService {
    private static final String BASE_URL = "http://api.hackathon.com/";

    private RetrofitServiceInterface service;
    private Retrofit retrofit;

    public RetrofitService(){

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitServiceInterface.class);
    }


    private class CallbackHandler<T> implements retrofit2.Callback<T> {

        private Retrofit retrofit;
        private Callback callback;

        public CallbackHandler(Retrofit retrofit, Callback callback) {
            this.retrofit = retrofit;
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if(response.isSuccessful()) {
                callback.onSuccess(response.body());
            } else {
                Converter<ResponseBody, Error> errorConverter =
                        retrofit.responseBodyConverter(Error.class, new Annotation[0]);
                try {
                    Error error = errorConverter.convert(response.errorBody());
                    callback.onFailure(error);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Error error = new Error(t.getMessage());
            callback.onFailure(error);
        }
    }


}
