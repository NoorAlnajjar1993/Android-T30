package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class BackgroundImagesProfileResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "isFree")
    val isFree: Boolean,
    @field:Json(name = "price")
    val price: Float,
    @field:Json(name = "imageUrl")
    val imageUrl: String,
    @field:Json(name = "currencyId")
    val currencyId: Int,
    var isSelected: Boolean = false
)
