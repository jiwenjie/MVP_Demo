package com.example.root.mvp_demo.utils;

import com.example.root.mvp_demo.ApiInterface.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author:Jiwenjie
 * email:Jiwenjie97@gmail.com
 * time:2018/10/21
 * desc: 有关网络请求的 Util
 * version:1.0
 */
public class RetrofitManager {

    private static ApiService apiService;

    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJaveCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

    private static OkHttpClient getClient() {
        // 可以设置请求过滤的水平，body，basic，headers

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)    // 日志，所有请求响应都看到
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .build();
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getClient())
                    .baseUrl("http://www.wanandroid.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJaveCallAdapterFactory)
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}














