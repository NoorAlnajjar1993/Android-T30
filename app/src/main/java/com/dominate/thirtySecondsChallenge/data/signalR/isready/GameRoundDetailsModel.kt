package com.dominate.thirtySecondsChallenge.data.signalR.isready

import kotlinx.serialization.Serializable

@Serializable
data class GameRoundDetailsModel(
    val Id: Int?,
    val FirstPlayerStrikes: Int?,
    val SecondPlayerStrikes: Int?,
    val FirstPlayerPoints: Int?,
    val SecondPlayerPoints: Int?,
    val SortIndex: Int?,
    val IsFinished: Int?,
    val TimerToAnswer: Int?,
    val TimerForTheBell: Int?,
    val TimerToShowQuestion: Int?,
    val IsRunning: Boolean?,
    val GamePlayId: Int?,
    val GamePlayTitle: String?,
    val ChoicesNumberToDisplay: Int?,
    val CorrectAnswerPercentage: Int?,
    val StrikesToLost: Int?,
    val RewardPoint: Int?,

)
