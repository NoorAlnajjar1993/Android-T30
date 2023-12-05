package com.dominate.thirtySecondsChallenge.data.signalR.creategame

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCreateGame(

    val title : String,
    val messageType : Int,
    val jsonData : String,


)
