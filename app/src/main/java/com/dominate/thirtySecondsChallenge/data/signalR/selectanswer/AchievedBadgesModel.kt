package com.dominate.thirtySecondsChallenge.data.signalR.selectanswer

import kotlinx.serialization.Serializable

@Serializable
data class AchievedBadgesModel(

    val Id: Int,
    val FileName: String,
    val ImageUrl: String

)
