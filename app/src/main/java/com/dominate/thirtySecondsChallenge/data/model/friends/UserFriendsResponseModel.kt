package com.dominate.thirtySecondsChallenge.data.model.friends

import com.squareup.moshi.Json

data class UserFriendsResponseModel(

    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "userPlayerId")
    val userPlayerId: Int,
    @field:Json(name = "friendUserPlayerId")
    val friendUserPlayerId: Int,
    @field:Json(name = "friendStatusId")
    val friendStatusId: Int,
    @field:Json(name = "userName")
    val userName: String,
    @field:Json(name = "firstName")
    val firstName: String,
    @field:Json(name = "lastName")
    val lastName: String,
    @field:Json(name = "phone")
    val phone: String,
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "profileImage")
    val profileImage: String,
    @field:Json(name = "isOnline")
    val isOnline: Boolean,
    @field:Json(name = "xp")
    val xp: Int,
    @field:Json(name = "userLevel")
    val userLevel: Int,
    @field:Json(name = "friendStatusTitle")
    val friendStatusTitle: String,
    @field:Json(name = "profileImageUrl")
    val profileImageUrl: String,
    @field:Json(name = "language")
    val language: String,
    var isCheck: Boolean = false
)
