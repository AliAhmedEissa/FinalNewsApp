package com.example.finalnewsapp.api;

import com.example.finalnewsapp.model.NewsRespnse.NewsResponse;
import com.example.finalnewsapp.model.SourceResponse.SourceResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebService {

    @GET("sources")
    Single<SourceResponse> getNewsSource(@Query("apiKey") String apiKey);

    @GET("everything")
    Single<NewsResponse> getNewsBySourceId(@Query("apiKey") String apiKey,
                                         @Query("sources") String sources);

}
