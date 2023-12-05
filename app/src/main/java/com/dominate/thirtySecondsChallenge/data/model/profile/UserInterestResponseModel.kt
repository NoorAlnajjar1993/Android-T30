package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class UserInterestResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "userPlayerId")
    val userPlayerId: Int,
    @field:Json(name = "categoryId")
    val categoryId: Int,
    @field:Json(name = "lastName")
    val image: String,
    @field:Json(name = "imageUrl")
    val imageUrl: String,
)
