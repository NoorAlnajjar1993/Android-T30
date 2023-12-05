package com.dominate.thirtySecondsChallenge.data.signalR.isready

import kotlinx.serialization.Serializable

@Serializable
data class GameDetailsModel(

    val Id: Int?,
    val FirstPlayerId: Int?,
    val FirstPlayerName: String?,
    val FirstPlayerImage: String?,
    val SecondPlayerId: Int?,
    val SecondPlayerName: String?,
    val SecondPlayerImage: String?,
    val ChampionshipId: Int?,
    val TournmentId: Int?,
    val WinnerPlayerId: Int?,
    val GameTypeId: Int?,
    val GameTypeTitle: String?,
    val QuestionLevelId: Int?,
    val QuestionLevelTitle: String?,
    val FirstPlayerIsReady: Boolean?,
    val SecondPlayerIsReady: Boolean?,
    val GameStatusId: Int?,
    val GameStatusTitle: String?,

    )
