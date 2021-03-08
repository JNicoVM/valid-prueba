package com.example.valid_prueba.useCases

import com.example.valid_prueba.models.request.TopArtistsRequest
import com.example.valid_prueba.repository.TopArtistsRepository
import com.google.gson.JsonObject
import io.reactivex.Observable

class GetAllTopArtistsUseCase(
    private val topArtistsRepository: TopArtistsRepository
) {

    fun invoke(topArtistsRequest: TopArtistsRequest): Observable<JsonObject> =
        topArtistsRepository.getAllTopArtists(topArtistsRequest)
}