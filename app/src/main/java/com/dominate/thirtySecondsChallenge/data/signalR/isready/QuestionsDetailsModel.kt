package com.dominate.thirtySecondsChallenge.data.signalR.isready

import kotlinx.serialization.Serializable

@Serializable
data class QuestionsDetailsModel(
    val Id: Int?,
    val IsShown: Boolean?,
    val GameRoundId: Int?,
    val QuestionId: Int?,
    val QuestionTitle: String?,
    val QuestionImage: String?,
    val QuestionLevelId: Int?,
    val QuestionTypeId: Int?,
    val Answers: List<AnswerModel>?,
)
