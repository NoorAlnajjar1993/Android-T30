package com.dominate.thirtySecondsChallenge.data.signalR.friendsrequest

import kotlinx.serialization.Serializable

@Serializable
data class JsonDataFriendsRequestResponse(

    val Message : String?,
    val NotificationCount : Int?,

)
