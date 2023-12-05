package com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest

import kotlinx.serialization.Serializable

@Serializable
data class JsonResponseError(
    val Message : String,
    val Code : Int,
)
