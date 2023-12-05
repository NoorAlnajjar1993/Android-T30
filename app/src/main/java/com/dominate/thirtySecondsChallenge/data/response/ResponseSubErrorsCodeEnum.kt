package com.dominate.thirtySecondsChallenge.data.response


enum class ResponseSubErrorsCodeEnum(val value: Int) {
    GENERAL_FAILED(-1),
    Success(200),
    PhoneNumberNotVerified(201),
    InvalidModel(1),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404);

    companion object {
        fun getResponseSubErrorsCodeEnumByValue(value: Int): ResponseSubErrorsCodeEnum {
            return when (value) {
                0 -> Success
                1 -> InvalidModel
                201 -> PhoneNumberNotVerified
                401 -> Unauthorized
                403 -> Forbidden
                404 -> NotFound
                else -> GENERAL_FAILED
            }
        }
    }
}