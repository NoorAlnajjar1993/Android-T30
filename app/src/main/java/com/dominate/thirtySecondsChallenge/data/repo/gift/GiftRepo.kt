package com.dominate.thirtySecondsChallenge.data.repo.gift

import com.dominate.thirtySecondsChallenge.data.model.gift.StickersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.gift.request.SendGiftsRequestModel
import com.dominate.thirtySecondsChallenge.data.model.home.ActiveGameTypesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.home.BannersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Part


interface GiftRepo {

    fun getAllGifts(
    ): Single<ResponseWrapper<List<GiftsResponseModel>>>

    fun sendGifts(
        sendGiftsRequestModel : SendGiftsRequestModel
    ): Single<ResponseWrapper<List<GiftsResponseModel>>>

    fun getStickers(
    ): Single<ResponseWrapper<List<StickersResponseModel>>>

}