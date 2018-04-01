package com.pcs.hackathonandroid.rest;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mufaddalgulshan on 31/03/18.
 */

public class RestClient {

    private static final String BASE_URL = "https://hackathondb.cbgq2jvb1nef.us-east-2.rds.amazonaws.com";
    private static final String WOWZA_BASE_URL = "https://api.cloud.wowza.com";
    private static Retrofit internal;
    private static Retrofit wowza;
    private static RestClient instance;

    public RestClient(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        int cacheSize = 2 * 1024 * 1024; // 2 MB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(cache)
                .build();

        internal = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        wowza = new Retrofit.Builder()
                .baseUrl(WOWZA_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RestClient getInstance(Context context) {
        if (instance == null)
            instance = new RestClient(context);
        return instance;
    }

    public <T> T getInternal(Class<T> service) {
        if (internal != null)
            return internal.create(service);
        return null;
    }

    public <T> T getWowza(Class<T> service) {
        if (wowza != null)
            return wowza.create(service);
        return null;
    }
}
