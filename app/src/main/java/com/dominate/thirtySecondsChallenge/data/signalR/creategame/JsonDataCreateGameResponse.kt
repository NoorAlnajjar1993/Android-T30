package com.dominate.thirtySecondsChallenge.data.signalR.creategame

import kotlinx.serialization.Serializable

@Serializable
data class JsonDataCreateGameResponse(

    val Id : Int?,
    val FirstPlayerId : Int?,
    val FirstPlayerName : String?,
    val FirstPlayerImage : String?,
    val SecondPlayerId : Int?,
    val SecondPlayerName : String?,
    val SecondPlayerImage : String?,
    val ChampionshipId : Int?,
    val TournmentId : Int?,
    val WinnerPlayerId : Int?,
    val GameTypeId : Int?,
    val GameTypeTitle : String?,
    val QuestionLevelId : Int?,
    val QuestionLevelTitle : String?,
    val FirstPlayerIsReady : Boolean?,
    val SecondPlayerIsReady : Boolean?,
    val GameStatusId : Int?,
    val GameStatusTitle : String?,
    val Language : String?,

)
