package com.dominate.thirtySecondsChallenge.data.model.profile

import com.squareup.moshi.Json

data class ProfileResponseModel(

    @field:Json(name = "userInfo")
    val userInfo: UserInfoModel? = null,
    @field:Json(name = "gifts")
    val gifts: List<GiftProfileResponseModel>? = null,
    @field:Json(name = "badges")
    val badges: List<BadgesResponseModel>? = null,
    @field:Json(name = "interests")
    val interests: List<InterestsResponseModel>? = null,
    @field:Json(name = "rewards")
    val rewards: List<RewardsResponseModel>? = null,
)
