package com.dominate.thirtySecondsChallenge.data.model.friends

import com.squareup.moshi.Json

data class UserBlockResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "userPlayerId")
    val userPlayerId: Int,
    @field:Json(name = "blockedUserPlayerId")
    val blockedUserPlayerId: Int,
    @field:Json(name = "blockedUserPlayerFirstName")
    val blockedUserPlayerFirstName: String,
    @field:Json(name = "blockedUserPlayerLastName")
    val blockedUserPlayerLastName: String,
)
