package com.dominate.thirtySecondsChallenge.data.model.user

import com.squareup.moshi.Json

data class UserDetailsResponseModel(

    @field:Json(name = "id")
    val id: Int,
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
    @field:Json(name = "xp")
    val xp: Int,
    @field:Json(name = "coins")
    val coins: Int,
    @field:Json(name = "diamonds")
    val diamonds: Int,
    @field:Json(name = "profileImageUrl")
    val profileImageUrl: String?,
    @field:Json(name = "friendSlots")
    val friendSlots: Int,
    @field:Json(name = "token")
    val token: String,
    @field:Json(name = "deviceId")
    val deviceId: String?,
    @field:Json(name = "isOnline")
    val isOnline: Boolean,
    @field:Json(name = "languageId")
    val languageId: Int,
    @field:Json(name = "countryId")
    val countryId: Int,
    @field:Json(name = "accountReference")
    val accountReference: String,
    @field:Json(name = "backgroundImageUrl")
    val backgroundImageUrl: String?,
    @field:Json(name = "level")
    val level: String?,
    @field:Json(name = "userLevelTypeTitle")
    val userLevelTypeTitle: String?,
    @field:Json(name = "friendStatusId")
    val friendStatusId: Int?,
    @field:Json(name = "userLevelTitle")
    val userLevelTitle: String?,
    @field:Json(name = "isFriend")
    val isFriend: Boolean,
    @field:Json(name = "questionLevelTitle")
    val questionLevelTitle: String,
    @field:Json(name = "questionLevelId")
    val questionLevelId: Int,
    @field:Json(name = "nextLevelXP")
    val nextLevelXP: Int,
    @field:Json(name = "unreadNotifications")
    val unReadNotifications: Int,
    @field:Json(name = "gameWinCount")
    val gameWinCount: Int,
    @field:Json(name = "sendGiftCount")
    val sendGiftCount: Int,
)
