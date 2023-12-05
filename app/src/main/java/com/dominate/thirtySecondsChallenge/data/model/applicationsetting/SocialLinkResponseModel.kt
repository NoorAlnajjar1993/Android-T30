package com.dominate.thirtySecondsChallenge.data.model.applicationsetting

import com.squareup.moshi.Json

data class SocialLinkResponseModel(

    @field:Json(name = "key")
    val key: String,
    @field:Json(name = "value")
    val value: String,
)