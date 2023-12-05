package com.dominate.thirtySecondsChallenge.data.signalR.creategame

import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class GameRequestModel(
    val GameTypeId: Int,
    val GameId: Int?=null,
)
