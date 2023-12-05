package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class UserInfoModel(

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
    val profileImageUrl: String,
    @field:Json(name = "friendSlots")
    val friendSlots: Int,
    @field:Json(name = "token")
    val token: String?,
    @field:Json(name = "deviceId")
    val deviceId: Int?,
    @field:Json(name = "isOnline")
    val isOnline: Boolean,
    @field:Json(name = "languageId")
    val languageId: Int,
    @field:Json(name = "countryId")
    val countryId: Int,
    @field:Json(name = "accountReference")
    val accountReference: String,
    @field:Json(name = "backgroundImageUrl")
    val backgroundImageUrl: String,
    @field:Json(name = "level")
    val level: Int,
    @field:Json(name = "userLevelTypeTitle")
    val userLevelTypeTitle: String,
    @field:Json(name = "userLevelTitle")
    val userLevelTitle: String,
    @field:Json(name = "friendStatusId")
    val friendStatusId: Int?,
    @field:Json(name = "isFriend")
    val isFriend: Boolean,
    @field:Json(name = "questionLevelTitle")
    val questionLevelTitle: String,
    @field:Json(name = "questionLevelId")
    val questionLevelId: Int,
    @field:Json(name = "sendGiftCount")
    val sendGiftCount: Int,
    @field:Json(name = "gamesWinCount")
    val gamesWinCount: Int,

    var countNumber: String,
    var color: String = "#15143E",
    var isTop: Boolean = false,
    var idBoard: Int,

)
