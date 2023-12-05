package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class ActiveLanguagesResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,
)
