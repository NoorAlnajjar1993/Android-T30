package com.dominate.thirtySecondsChallenge.data.model.store

import com.squareup.moshi.Json

data class MainCategoryResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "parentCategoryId")
    val parentCategoryId: Int?,
    @field:Json(name = "image")
    val image: String,
    @field:Json(name = "imageUrl")
    val imageUrl: String,
    var isSelected: Boolean = false
)
