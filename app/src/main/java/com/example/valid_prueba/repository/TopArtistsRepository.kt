package com.example.valid_prueba.repository

import com.example.valid_prueba.data.RemoteTopArtistsDataSource
import com.example.valid_prueba.models.request.TopArtistsRequest
import com.google.gson.JsonObject
import io.reactivex.Observable

class TopArtistsRepository(private val remoteTopArtistsDataSource: RemoteTopArtistsDataSource //fuente de datos remota
//private val searchDao: SearchDao // fuente de datos local (no room pero se pueda usar configurar en un futuro para eso)
) {

   fun getAllTopArtists(topArtistsRequest: TopArtistsRequest): Observable<JsonObject> =
       remoteTopArtistsDataSource.perfomSearchTopArtists(topArtistsRequest)
}