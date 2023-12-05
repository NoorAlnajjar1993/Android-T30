package com.dominate.thirtySecondsChallenge.data.signalR.general

import kotlinx.serialization.Serializable


@Serializable
data class ArgumentsResponse(
    val messageType: Int,
    val jsonData: String
)
