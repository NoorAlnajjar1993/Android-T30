package com.dominate.thirtySecondsChallenge.data.model.user.request


data class DtoRegisterRequest(

    var socialMediaId: String?,
    var deviceTypeId: Int,
    var firstName: String?,
    var lastName: String,
    var phone: String,
    var email: String,
    var intrests: List<Int>? = arrayListOf(),
    var ReferralCode: String?,
)
