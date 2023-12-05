package com.dominate.thirtySecondsChallenge.data.signalR.stickers

import kotlinx.serialization.Serializable

@Serializable
data class JsonDataStickersResponse(

    val SenderId : Int,
    val Sticker : StickersResponse

)
