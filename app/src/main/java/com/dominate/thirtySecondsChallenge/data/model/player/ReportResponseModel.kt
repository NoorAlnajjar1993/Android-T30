package com.dominate.thirtySecondsChallenge.data.model.player

import com.squareup.moshi.Json

data class ReportResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "userPlayerId")
    val userPlayerId: Int,
    @field:Json(name = "userPlayerFirstName")
    val userPlayerFirstName: String,
    @field:Json(name = "userPlayerLastName")
    val userPlayerLastName: String,
    @field:Json(name = "reportedUserPlayerId")
    val reportedUserPlayerId: Int,
    @field:Json(name = "reportedUserPlayerFirstName")
    val reportedUserPlayerFirstName: String?,
    @field:Json(name = "reportedUserPlayerLastName")
    val reportedUserPlayerLastName: String?,
    @field:Json(name = "reportReasonId")
    val reportReasonId: Int,
    @field:Json(name = "reportReasonTitle")
    val reportReasonTitle: String,
    @field:Json(name = "reportReasonDescription")
    val reportReasonDescription: String,
    @field:Json(name = "notes")
    val notes: String,
)
