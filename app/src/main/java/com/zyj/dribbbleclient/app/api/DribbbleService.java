package com.zyj.dribbbleclient.app.api;

import com.zyj.dribbbleclient.app.model.Shot;

import retrofit.Call;
import retrofit.http.GET;

public interface DribbbleService {

    @GET("shots/")
    Call<Shot[]> shotList();
}
