package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class BadgesResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "userPlayerId")
    val userPlayerId: Int,
    @field:Json(name = "isVisible")
    val isVisible: Boolean,
    @field:Json(name = "badgeId")
    val badgeId: Int,
    @field:Json(name = "image")
    val image: String?,
    @field:Json(name = "imageUrl")
    val imageUrl: String?,
    @field:Json(name = "language")
    val language: String?,
    var isSelected: Boolean = false


)
