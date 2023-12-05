package com.dominate.thirtySecondsChallenge.data.signalR.selectanswer

import kotlinx.serialization.Serializable

@Serializable
data class SelectAnswerDataRequest(

    val GameId: Int,
    val GameRoundId: Int,
    val GamePlayId: Int,
    val QuestionId: Int,
    val AnswerId: Int,
    val AnswerTitle: String,
    val TimeOut: Boolean,
    val Pass: Boolean,
    val TimeToAnswer: Int,

)
