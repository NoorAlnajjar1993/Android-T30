package com.dominate.thirtySecondsChallenge.data.model.friends

import com.squareup.moshi.Json

data class ReferralResponseModel(

    @field:Json(name = "userPlayerId")
    val userPlayerId: Int,
    @field:Json(name = "referralCode")
    val referralCode: Int,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "isActive")
    val isActive: Boolean,
    @field:Json(name = "isDeleted")
    val isDeleted: Boolean,
    @field:Json(name = "partition")
    val partition: Int,
    @field:Json(name = "createdBy")
    val createdBy: Int,
    @field:Json(name = "createdDate")
    val createdDate: String,
    @field:Json(name = "createdDateTimestamp")
    val createdDateTimestamp: Long,
    @field:Json(name = "lastModifiedBy")
    val lastModifiedBy: Int?,
    @field:Json(name = "lastModifiedDate")
    val lastModifiedDate: String,
    @field:Json(name = "lastModifiedDateTimestamp")
    val lastModifiedDateTimestamp: Int?,

)
