package com.pcs.hackathonandroid.rest;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.icu.lang.UCharacter.LineBreak.SPACE;
import static android.icu.lang.UProperty.LINE_BREAK;

/**
 * Created by mufaddalgulshan on 31/03/18.
 */

public class RestClient {

    private static final String BASE_URL = "hackathondb.cbgq2jvb1nef.us-east-2.rds.amazonaws.com/api/";
    private static Retrofit retrofit;
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

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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

    public <T> T get(Class<T> service) {
        if (retrofit != null)
            return retrofit.create(service);
        return null;
    }
}
