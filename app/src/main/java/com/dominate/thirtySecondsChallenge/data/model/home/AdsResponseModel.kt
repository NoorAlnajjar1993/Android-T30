package com.dominate.thirtySecondsChallenge.data.model.home

import com.squareup.moshi.Json

data class AdsResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "image")
    val image: String,
    @field:Json(name = "imageUrl")
    val imageUrl: String,
    @field:Json(name = "redirectUrl")
    val redirectUrl: String?,
)
