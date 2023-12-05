package com.dominate.thirtySecondsChallenge.data.signalR.selectanswer

import com.dominate.thirtySecondsChallenge.data.signalR.isready.GameDetailsModel
import com.dominate.thirtySecondsChallenge.data.signalR.isready.GameRoundDetailsModel
import com.dominate.thirtySecondsChallenge.data.signalR.isready.QuestionsDetailsModel
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class JsonDataIsSelectAnswerResponse(

    val PlayerTurn: Int?,
    val IsCorrectAnswer: Boolean?, // اذا الجواب صح او لا لما انا اجاوب
    val IsGameFinished: Boolean?,// انتهاء كل اللعبة مع النتيجة
    val HaveNewQuestion: Boolean?,// اذا الاجوبة خلصت وجاوبت
    val HaveNewRound: Boolean?,// اذا اللعبة الاولى خلصت
    val PreviousAnswerTitle: String?,// اذا الجواب للطرف الثاني
    val Game: GameDetailsModel?,
    val GameRound: GameRoundDetailsModel,
    val Question: QuestionsDetailsModel,
    val GameResult: GameResultModel,
    val AllowPass: Boolean,
    var AchievedBadges : List<AchievedBadgesModel>?
)
