package com.dominate.thirtySecondsChallenge.data.daos.remote.auth

import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.LoginRequestModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.RegisterRequestModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserRemoteDao {

    @POST("api/UserPlayer/Login")
    fun login(
        @Body loginRequestModel: LoginRequestModel
    ): Single<ResponseWrapper<UserDetailsResponseModel>>

    @POST("api/UserPlayer/Register")
    fun register(
        @Body registerRequestModel: RegisterRequestModel
    ): Single<ResponseWrapper<UserDetailsResponseModel>>

    @POST("api/UserPlayer/Logout")
    fun logout(
    ): Single<ResponseWrapper<String>>


}
