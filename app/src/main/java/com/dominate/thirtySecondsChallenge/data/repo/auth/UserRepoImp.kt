package com.dominate.thirtySecondsChallenge.data.repo.auth

import com.dominate.thirtySecondsChallenge.data.daos.remote.auth.UserRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.user.UserDetailsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.LoginRequestModel
import com.dominate.thirtySecondsChallenge.data.model.user.request.RegisterRequestModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class UserRepoImp @Inject constructor(
    private val userRemoteDao: UserRemoteDao,
) : BaseRepo(), UserRepo {

    override fun login(loginRequestModel: LoginRequestModel): Single<ResponseWrapper<UserDetailsResponseModel>> {
        return userRemoteDao.login(loginRequestModel)
    }

    override fun register(registerRequestModel: RegisterRequestModel): Single<ResponseWrapper<UserDetailsResponseModel>> {
        return userRemoteDao.register(registerRequestModel)
    }

}