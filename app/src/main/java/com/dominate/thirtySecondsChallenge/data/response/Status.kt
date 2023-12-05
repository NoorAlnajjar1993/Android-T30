package com.dominate.thirtySecondsChallenge.data.response

import com.squareup.moshi.Json

data class Status(

    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "code")
    val code: Int,

    )
