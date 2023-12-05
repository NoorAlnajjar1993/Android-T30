package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class GiftsResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "isActive")
    val isActive: Boolean,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "currencyId")
    val currencyId: Int,
    @field:Json(name = "isFree")
    val isFree: Boolean,
    @field:Json(name = "price")
    val price: Float,
    @field:Json(name = "image")
    val image: String,
    @field:Json(name = "imageUrl")
    val imageUrl: String,
    var isSelected: Boolean = false
)
