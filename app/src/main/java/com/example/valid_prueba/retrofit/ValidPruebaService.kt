package com.example.valid_prueba.retrofit


import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ValidPruebaService {

    /** API SEARCH*/
    @GET("/2.0/?method=geo.gettopartists")
    fun doSearchTopArtists(
        @Query("country") country: String,
        @Query("api_key") apiKey: String,
        @Query("limit") limit: String = "",
        @Query("page") page: String = "",
        @Query("format") json: String = "json"
    ): Observable<JsonObject>

}