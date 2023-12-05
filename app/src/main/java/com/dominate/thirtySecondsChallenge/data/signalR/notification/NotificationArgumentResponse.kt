package com.dominate.thirtySecondsChallenge.data.signalR.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationArgumentResponse(
    val arguments : List<String>
)
