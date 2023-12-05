package com.dominate.thirtySecondsChallenge.data.model.home

import com.squareup.moshi.Json

data class ActiveGameTypesResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,

    )
