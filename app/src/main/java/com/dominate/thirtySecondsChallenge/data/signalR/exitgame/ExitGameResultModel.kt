package com.dominate.thirtySecondsChallenge.data.signalR.exitgame

import kotlinx.serialization.Serializable

@Serializable
data class ExitGameResultModel(
    val WinnerId: Int,
    val FirstPlayerPoints: Int,
    val SecoundPlayerPoints: Int,
    val GameId: Int,
)
