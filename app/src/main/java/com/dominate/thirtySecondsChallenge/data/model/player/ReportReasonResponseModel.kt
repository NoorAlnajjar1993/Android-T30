package com.dominate.thirtySecondsChallenge.data.model.player

import com.squareup.moshi.Json

data class ReportReasonResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    var isSelected : Boolean = false
)
