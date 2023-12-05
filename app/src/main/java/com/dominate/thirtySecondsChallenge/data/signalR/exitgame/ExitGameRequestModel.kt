package com.dominate.thirtySecondsChallenge.data.signalR.exitgame

import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class ExitGameRequestModel(
    val GameId: Int
)
