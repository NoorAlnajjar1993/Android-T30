package com.dominate.thirtySecondsChallenge.data.signalR.isready

import kotlinx.serialization.Serializable

@Serializable
data class AnswerModel(
    val Id: Int?,
    val Title: String?,
    val QuestionId: Int?,
    val AnswerId: Int?,
    val AnswerTitle: String?,
    val QuestionTypeId: Int?,
    val IsCorrect: Boolean?,
    val IsShown: Boolean?,
    var isSelected: Boolean = false
)
