package com.zyj.dribbbleclient.app.api

import retrofit.GsonConverterFactory
import retrofit.Retrofit

/**
 * Created by zhaoyuanjie on 15/9/23.
 */

object Restful {

    val baseUrl = "http://api.dribbble.com/"
    val retrofitClient: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getService(): DribbbleService {
        return retrofitClient.create(DribbbleService::class.java)
    }
}