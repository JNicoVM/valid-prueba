package com.example.valid_prueba.data

import com.example.valid_prueba.models.request.TopArtistsRequest
import com.google.gson.JsonObject
import io.reactivex.Observable

interface RemoteTopArtistsDataSource {
    fun perfomSearchTopArtists(topArtistsRequest: TopArtistsRequest) : Observable<JsonObject>
}