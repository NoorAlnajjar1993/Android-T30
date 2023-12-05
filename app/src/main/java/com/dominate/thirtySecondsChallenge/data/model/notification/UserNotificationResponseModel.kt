package com.dominate.thirtySecondsChallenge.data.model.notification

import com.squareup.moshi.Json

data class UserNotificationResponseModel(

    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "isRead")
    val isRead: Boolean,
    @field:Json(name = "userPlayerUserId")
    val userPlayerUserId: Int,
    @field:Json(name = "notificationTypeId")
    val notificationTypeId: Int,
    @field:Json(name = "userPlayerUserFriendRequestId")
    val userPlayerUserFriendRequestId: Int?,
    @field:Json(name = "friendStatusId")
    val friendStatusId: Int,
    @field:Json(name = "userFriendId")
    val userFriendId: Int,

)
