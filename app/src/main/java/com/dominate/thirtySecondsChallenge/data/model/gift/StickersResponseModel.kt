package com.dominate.thirtySecondsChallenge.data.model.gift

import com.squareup.moshi.Json

data class StickersResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "image")
    val image: String,
    @field:Json(name = "imageUrl")
    val imageUrl: String,
    @field:Json(name = "allowedLevel")
    val allowedLevel: Int,
    @field:Json(name = "language")
    val language: String,
    var isSelected: Boolean = false
)
