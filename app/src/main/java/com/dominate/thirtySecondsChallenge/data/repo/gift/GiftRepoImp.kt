package com.dominate.thirtySecondsChallenge.data.repo.gift

import com.dominate.thirtySecondsChallenge.data.daos.remote.gift.GiftRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.gift.StickersResponseModel
import com.dominate.thirtySecondsChallenge.data.model.gift.request.SendGiftsRequestModel
import com.dominate.thirtySecondsChallenge.data.model.profile.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class GiftRepoImp @Inject constructor(
    private val giftRemoteDao: GiftRemoteDao
) : BaseRepo(), GiftRepo {

    override fun getAllGifts(): Single<ResponseWrapper<List<GiftsResponseModel>>> {
        return giftRemoteDao.getAllGifts()
    }

    override fun sendGifts(sendGiftsRequestModel: SendGiftsRequestModel): Single<ResponseWrapper<List<GiftsResponseModel>>> {
        return giftRemoteDao.sendGifts(sendGiftsRequestModel)
    }

    override fun getStickers(): Single<ResponseWrapper<List<StickersResponseModel>>> {
        return giftRemoteDao.getStickers()
    }

}