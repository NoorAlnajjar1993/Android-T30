package com.dominate.thirtySecondsChallenge.data.signalR.selectanswer

import kotlinx.serialization.Serializable

@Serializable
data class GameResultModel(
    val WinnerId: Int,
    val FirstPlayerPoints: Int,
    val SecoundPlayerPoints: Int,
    val FirstPlayerXp: Int,
    val SecoundPlayerXp: Int,
    val FirstPlayerCoins: Int,
    val SecoundPlayerCoins: Int,
    val FirstPlayerLevel: Int,
    val SecoundPlayerLevel: Int,
)
