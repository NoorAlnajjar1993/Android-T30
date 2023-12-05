package com.dominate.thirtySecondsChallenge.data.signalR.stickers

import kotlinx.serialization.Serializable

@Serializable
data class StickersResponse(

    val Id: Int,
    val Title: String,
    val Description: String,
    val Image: String,
    val ImageUrl: String,
    val AllowedLevel: Int,

)
