package com.dominate.thirtySecondsChallenge.data.daos.remote.store

import com.dominate.thirtySecondsChallenge.data.model.player.ReportReasonResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.ReportResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.ProfileResponseModel
import com.dominate.thirtySecondsChallenge.data.model.player.request.ReportPlayerAddRequestModel
import com.dominate.thirtySecondsChallenge.data.model.store.AccessoriesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.CoinsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.DiamondsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.MainCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.OffersResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path

interface StoreRemoteDao {

    @GET("api/Store/MainCategory")
    fun getMainCategory(
    ): Single<ResponseWrapper<List<MainCategoryResponseModel>>>

    @GET("api/Store/Items/Coins")
    fun getItemsCoins(
    ): Single<ResponseWrapper<List<CoinsResponseModel>>>

    @GET("api/Store/Items/Diamonds")
    fun getItemsDiamonds(
    ): Single<ResponseWrapper<List<DiamondsResponseModel>>>

    @GET("api/Store/Items/Offers")
    fun getItemsOffers(
    ): Single<ResponseWrapper<List<OffersResponseModel>>>

    @GET("api/Store/Items/Gifts")
    fun getItemsGifts(
    ): Single<ResponseWrapper<List<GiftsResponseModel>>>

    @GET("api/Store/Items/Accessories/Stickers")
    fun getStickers(
    ): Single<ResponseWrapper<List<AccessoriesResponseModel>>>

    @GET("api/Store/Items/Accessories/Backgrounds")
    fun getBackgrounds(
    ): Single<ResponseWrapper<List<AccessoriesResponseModel>>>


}
