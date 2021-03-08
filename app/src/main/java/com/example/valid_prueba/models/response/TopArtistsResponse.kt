package com.example.valid_prueba.models.response

import com.google.gson.annotations.SerializedName

data class TopArtistsResponse(
    @SerializedName("name")
    val name : String,
    @SerializedName("listeners")
    val listeners : String,
    @SerializedName("url")
    val url : String,
    @SerializedName("streamable")
    val streamable : Int,
    @SerializedName("image")
    val image : ArrayList<ImageResponse>
)

data class ImageResponse(
    @SerializedName("#text")
    val imageUrl : String
)