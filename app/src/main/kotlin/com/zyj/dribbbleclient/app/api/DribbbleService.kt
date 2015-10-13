package com.zyj.dribbbleclient.app.api

import com.zyj.dribbbleclient.app.model.Shot

import retrofit.http.GET
import rx.Observable

interface DribbbleService {

    @GET("shots/")
    fun shotList(): Observable<Array<Shot>>
}
