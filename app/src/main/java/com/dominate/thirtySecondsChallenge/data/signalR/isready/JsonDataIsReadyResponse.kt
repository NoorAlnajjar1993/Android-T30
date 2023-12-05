package com.dominate.thirtySecondsChallenge.data.signalR.isready

import kotlinx.serialization.Serializable

@Serializable
data class JsonDataIsReadyResponse(
    val IsGameReady : Boolean,
    val PlayerTurn : Int?,
    val GameDetails : GameDetailsModel,
    val GameRoundDetails: GameRoundDetailsModel,
    val QuestionsDetails: QuestionsDetailsModel,

)
