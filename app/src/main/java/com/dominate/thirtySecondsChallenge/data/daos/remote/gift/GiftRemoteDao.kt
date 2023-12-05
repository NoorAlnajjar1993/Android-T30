package com.dominate.thirtySecondsChallenge.data.daos.remote.gift

import com.dominate.thirtySecondsChallenge.data.model.gift.StickersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.gift.request.SendGiftsRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST

interface GiftRemoteDao {


    @GET("api/Gifts/GetAll")
    fun getAllGifts(
    ): Single<ResponseWrapper<List<GiftsResponseModel>>>

    @POST("api/UserGifts/Send")
    fun sendGifts(
        @Body sendGiftsRequestModel : SendGiftsRequestModel
    ): Single<ResponseWrapper<List<GiftsResponseModel>>>

    @GET("api/Stickers/GetStickers")
    fun getStickers(
    ): Single<ResponseWrapper<List<StickersResponseModel>>>


}
