package com.dominate.thirtySecondsChallenge.data.model.menu

import com.squareup.moshi.Json

data class CategoryGetMainResponseModel(

    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "title")
    val title: String?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "isVisible")
    val isVisible: Boolean?,
    @field:Json(name = "image")
    val image: String?,
    @field:Json(name = "imageUrl")
    val imageUrl: String?,
    @field:Json(name = "language")
    val language: String?,
)
