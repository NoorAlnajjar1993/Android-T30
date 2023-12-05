package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class FaqCategoryResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    var isSelected:Boolean = false

)
