package com.dominate.thirtySecondsChallenge.data.model.leader

import com.squareup.moshi.Json

data class LeaderboardFiltersModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
)
