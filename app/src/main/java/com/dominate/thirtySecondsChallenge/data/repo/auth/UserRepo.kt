package com.dominate.thirtySecondsChallenge.data.repo.auth

import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.LoginRequestModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.RegisterRequestModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single


interface UserRepo {

    fun login(
        loginRequestModel: LoginRequestModel,
    ): Single<ResponseWrapper<UserDetailsResponseModel>>

    fun register(
        registerRequestModel: RegisterRequestModel
    ): Single<ResponseWrapper<UserDetailsResponseModel>>

}