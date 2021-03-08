package com.example.valid_prueba.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRequest<T: Any>(
    baseUrl: String
) {

    private val interceptor= HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val cliente=OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(40,TimeUnit.SECONDS)
        .build()

    val interfazApi: ValidPruebaService= Retrofit.Builder()
        .client(cliente)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(ValidPruebaService::class.java)
}

class RequestTopArtists(baseUrl: String): BaseRequest<ValidPruebaService>(baseUrl)

