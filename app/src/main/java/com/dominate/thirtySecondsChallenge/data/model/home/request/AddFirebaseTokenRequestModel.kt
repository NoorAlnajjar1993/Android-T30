package com.dominate.thirtySecondsChallenge.data.model.home.request

import com.squareup.moshi.Json

data class AddFirebaseTokenRequestModel(
    @field:Json(name = "userPlayerId")
    val userPlayerId: Int?,
    @field:Json(name = "firebaseToken")
    val firebaseToken: String,
)