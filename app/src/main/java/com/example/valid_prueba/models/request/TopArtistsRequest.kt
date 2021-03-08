package com.example.valid_prueba.models.request

data class TopArtistsRequest(
    val country : String,
    val limit : String = "",
    val page : Int = 0,
    val apiKey : String
)