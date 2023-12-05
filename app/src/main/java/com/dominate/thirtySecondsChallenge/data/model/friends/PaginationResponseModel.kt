package com.dominate.thirtySecondsChallenge.data.model.friends

import com.dominate.thirtySecondsChallenge.data.model.profile.UserInfoModel
import com.squareup.moshi.Json

data class PaginationResponseModel(

    @field:Json(name = "currentPage")
    val currentPage: Int,
    @field:Json(name = "userList")
    val userList: List<UserInfoModel>,
)
