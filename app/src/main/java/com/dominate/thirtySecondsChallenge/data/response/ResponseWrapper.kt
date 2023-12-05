package com.dominate.thirtySecondsChallenge.data.response

import com.squareup.moshi.Json

data class ResponseWrapper<RETURN_MODEL>(
    @field:Json(name = "data")
    val data: RETURN_MODEL?,
    @field:Json(name = "succeeded")
    val succeeded: Boolean,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "error")
    val error: Status?,
)