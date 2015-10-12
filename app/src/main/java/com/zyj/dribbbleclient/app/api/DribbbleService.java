package com.zyj.dribbbleclient.app.api;

import com.zyj.dribbbleclient.app.model.Shot;

import retrofit.http.GET;
import rx.Observable;

public interface DribbbleService {

    @GET("shots/")
    Observable<Shot[]> shotList();
}
