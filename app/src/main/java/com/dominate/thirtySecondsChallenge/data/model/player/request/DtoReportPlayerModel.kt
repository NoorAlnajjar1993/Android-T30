package com.dominate.thirtySecondsChallenge.data.model.player.request

data class DtoReportPlayerModel(

    var reportedUserPlayerId: Int,
    var reportReasonId: Int,
    var notes: String,

    )
