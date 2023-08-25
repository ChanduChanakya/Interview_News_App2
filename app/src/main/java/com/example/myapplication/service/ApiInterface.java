package com.example.myapplication.service;

import com.example.myapplication.model.NewsArticlesMainResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("v2/everything")
    Call<NewsArticlesMainResponse> getNewsArticles(@Query("q") String q,
                                                              @Query("from") String from,
                                                              @Query("sortBy") String sortBy,
                                                              @Query("apiKey") String apiKey);
}
