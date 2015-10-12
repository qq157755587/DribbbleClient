package com.zyj.dribbbleclient.app.api

import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

/**
 * Created by zhaoyuanjie on 15/9/23.
 */

public object Restful {

    fun getService(): DribbbleService {
        val client:OkHttpClient = OkHttpClient();
        client.networkInterceptors().add(object: Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response? {
                val originalRequest:Request = chain!!.request()
                if (originalRequest.header("Authorization") != null) {
                    return chain.proceed(originalRequest)
                }
                val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer 8aac5e2678da4b5dda86f5c558d6b7e368d138ba5df7fe8e3c351bb640e68721")
                        .build()
                return chain.proceed(newRequest)
            }
        })
        val retrofitClient: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.dribbble.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
        return retrofitClient.create(DribbbleService::class.java)
    }
}