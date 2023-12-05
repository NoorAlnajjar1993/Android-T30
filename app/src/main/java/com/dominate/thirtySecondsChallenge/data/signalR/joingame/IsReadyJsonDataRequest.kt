package com.dominate.thirtySecondsChallenge.data.signalR.joingame

import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class IsReadyJsonDataRequest(
    val GameId: Int
)
