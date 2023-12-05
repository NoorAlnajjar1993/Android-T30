package com.dominate.thirtySecondsChallenge.data.repo.store

import com.dominate.thirtySecondsChallenge.data.model.store.AccessoriesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.CoinsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.DiamondsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.MainCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.OffersResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.GET


interface StoreRepo {

    fun getMainCategory(
    ): Single<ResponseWrapper<List<MainCategoryResponseModel>>>

    fun getItemsCoins(
    ): Single<ResponseWrapper<List<CoinsResponseModel>>>

    fun getItemsDiamonds(
    ): Single<ResponseWrapper<List<DiamondsResponseModel>>>

    fun getItemsOffers(
    ): Single<ResponseWrapper<List<OffersResponseModel>>>

    fun getItemsGifts(
    ): Single<ResponseWrapper<List<GiftsResponseModel>>>

    fun getStickers(
    ): Single<ResponseWrapper<List<AccessoriesResponseModel>>>

    fun getBackgrounds(
    ): Single<ResponseWrapper<List<AccessoriesResponseModel>>>
}