package com.dominate.thirtySecondsChallenge.data.signalR.stickers

import kotlinx.serialization.Serializable

@Serializable
data class SelectStickersDataRequest(

    val GameId: Int,
    val StickerId: Int,

)
