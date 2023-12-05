package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class GiftProfileResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "userPlayerId")
    val userPlayerId: Int,
    @field:Json(name = "isVisible")
    val isVisible: Boolean,
    @field:Json(name = "giftId")
    val giftId: Int,
    @field:Json(name = "title")
    val title: String?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "isFree")
    val isFree: Boolean?,
    @field:Json(name = "price")
    val price: Float?,
    @field:Json(name = "image")
    val image: String?,
    @field:Json(name = "imageUrl")
    val imageUrl: String?,
    @field:Json(name = "count")
    val count: Int?,
    @field:Json(name = "language")
    val language: String?,
    var isSelected: Boolean = false

)