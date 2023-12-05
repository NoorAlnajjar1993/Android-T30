package com.dominate.thirtySecondsChallenge.data.daos.remote.home

import com.dominate.thirtySecondsChallenge.data.model.home.ActiveBannersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.ActiveGameTypesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.BannersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.HomeResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.request.AddFirebaseTokenRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.request.EditProfileUserInfoRequestModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeRemoteDao {

    @GET("api/Banner/GetBanners")
    fun getBanners(
    ): Single<ResponseWrapper<List<BannersResponseModel>>>

    @GET("api/Home")
    fun getHome(
    ): Single<ResponseWrapper<HomeResponseModel>>

    @GET("api/Lookups/GetActiveGameTypes")
    fun getActiveGameTypes(
    ): Single<ResponseWrapper<List<ActiveGameTypesResponseModel>>>

    @GET("Banner/GetActiveBanners")
    fun getActiveBanners(
    ): Single<ResponseWrapper<ActiveBannersResponseModel>>

    @POST("api/FirebaseToken/Add")
    fun addFirebaseToken(
        @Body addFirebaseToken:AddFirebaseTokenRequestModel
    ): Single<ResponseWrapper<Boolean>>


}
