package com.dominate.thirtySecondsChallenge.data.repo.home

import com.dominate.thirtySecondsChallenge.data.model.home.ActiveGameTypesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.BannersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.HomeResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.request.AddFirebaseTokenRequestModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST


interface HomeRepo {

    fun getBanners(
    ): Single<ResponseWrapper<List<BannersResponseModel>>>

    fun getActiveGameTypes(
    ): Single<ResponseWrapper<List<ActiveGameTypesResponseModel>>>

    fun getHome(
    ): Single<ResponseWrapper<HomeResponseModel>>

    fun addFirebaseToken(
        addFirebaseToken: AddFirebaseTokenRequestModel
    ): Single<ResponseWrapper<Boolean>>

}