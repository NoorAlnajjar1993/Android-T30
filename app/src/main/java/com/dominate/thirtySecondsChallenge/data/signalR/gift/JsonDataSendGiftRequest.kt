package com.dominate.thirtySecondsChallenge.data.signalR.gift

import kotlinx.serialization.Serializable

@Serializable
data class JsonDataSendGiftRequest(

    val UserPlayerId : Int?,
    val GiftId : Int?,

)
