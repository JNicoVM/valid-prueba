package com.example.valid_prueba.retrofit

import android.util.Log
import com.example.valid_prueba.data.RemoteTopArtistsDataSource
import com.example.valid_prueba.models.request.TopArtistsRequest
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SearchRetrofitDataSource(
    private val requestTopArtists: RequestTopArtists
) : RemoteTopArtistsDataSource {

    fun configuracionRx(jsonObjectObservable: Observable<JsonObject>): Observable<JsonObject>
            =jsonObjectObservable.subscribeOn(Schedulers.newThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .onErrorReturn(this::obtenerError)

    private fun obtenerError(throwable: Throwable): JsonObject {
        val error="${throwable.message} Class: ${throwable.javaClass.name}"
        Log.e("Api Error",error,throwable)
        val jsonObject= JsonObject()
        val mensajeError= when(throwable) {
            is SocketTimeoutException ->
                Log.e("Time out", "No internet connection")
            is UnknownHostException ->
                Log.e("Time out", "No internet connection")
            else ->
                Log.e("unx error", "Might be an error")
        }
        jsonObject.addProperty("response",mensajeError)
        return jsonObject
    }

    override fun perfomSearchTopArtists(topArtistsRequest: TopArtistsRequest): Observable<JsonObject> {
        return configuracionRx(requestTopArtists.interfazApi
            .doSearchTopArtists(topArtistsRequest.country, topArtistsRequest.apiKey))

    }

}