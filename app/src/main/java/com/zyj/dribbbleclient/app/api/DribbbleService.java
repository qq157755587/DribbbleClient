package com.zyj.dribbbleclient.app.api;

import com.zyj.dribbbleclient.app.model.Shots;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface DribbbleService {
    String POPULAR = "popular";
    String DEBUTS = "debuts";
    String EVERYONE = "everyone";

    @GET("shots/{type}")
    Call<Shots> shotList(@Path("type") String type, @Query("page") int page);
}
