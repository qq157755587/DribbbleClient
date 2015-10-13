package com.zyj.dribbbleclient.app.api

import com.zyj.dribbbleclient.app.model.Shot

import retrofit.http.GET
import retrofit.http.Query
import rx.Observable

interface DribbbleService {

    @GET("shots/")
    fun shotList(@Query("list") list: String? = null,
                 @Query("sort") sort: String? = null): Observable<Array<Shot>>
}
