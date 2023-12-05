package com.dominate.thirtySecondsChallenge.data.model.home

import com.squareup.moshi.Json

data class BannersResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "backgroundImage")
    val backgroundImage: String,
    @field:Json(name = "backgroundImageUrl")
    val backgroundImageUrl: String,
    @field:Json(name = "redirectUrl")
    val redirectUrl: String,
    @field:Json(name = "championshipId")
    val championshipId: Int,
    @field:Json(name = "tournmenetId")
    val tournmenetId: Int,
    @field:Json(name = "bannerTypeId")
    val bannerTypeId: Int,
    @field:Json(name = "bannerType")
    val bannerType: String,

)
