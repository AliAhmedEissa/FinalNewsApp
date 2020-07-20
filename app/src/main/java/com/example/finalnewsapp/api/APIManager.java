package com.example.finalnewsapp.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {

    private static Retrofit retrofit;

    private static Retrofit getInstance(){
        if (retrofit == null ){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://newsapi.org/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static WebService getApis(){
        return getInstance().create(WebService.class);
    }

}
