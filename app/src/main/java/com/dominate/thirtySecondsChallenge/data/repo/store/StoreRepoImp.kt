package com.dominate.thirtySecondsChallenge.data.repo.store

import com.dominate.thirtySecondsChallenge.data.daos.remote.store.StoreRemoteDao
import com.dominate.thirtySecondsChallenge.data.model.store.AccessoriesResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.CoinsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.DiamondsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.GiftsResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.MainCategoryResponseModel
import com.dominate.thirtySecondsChallenge.data.model.store.OffersResponseModel
import com.dominate.thirtySecondsChallenge.data.repo.base.BaseRepo
import com.dominate.thirtySecondsChallenge.data.response.ResponseWrapper
import io.reactivex.Single
import javax.inject.Inject


class StoreRepoImp @Inject constructor(
    private val storeRemoteDao: StoreRemoteDao
) : BaseRepo(), StoreRepo {

    override fun getMainCategory(): Single<ResponseWrapper<List<MainCategoryResponseModel>>> {
        return storeRemoteDao.getMainCategory()
    }

    override fun getItemsCoins(): Single<ResponseWrapper<List<CoinsResponseModel>>> {
        return storeRemoteDao.getItemsCoins()
    }

    override fun getItemsDiamonds(): Single<ResponseWrapper<List<DiamondsResponseModel>>> {
        return storeRemoteDao.getItemsDiamonds()
    }

    override fun getItemsOffers(): Single<ResponseWrapper<List<OffersResponseModel>>> {
        return storeRemoteDao.getItemsOffers()
    }

    override fun getItemsGifts(): Single<ResponseWrapper<List<GiftsResponseModel>>> {
        return storeRemoteDao.getItemsGifts()
    }

    override fun getStickers(): Single<ResponseWrapper<List<AccessoriesResponseModel>>> {
        return storeRemoteDao.getStickers()
    }

    override fun getBackgrounds(): Single<ResponseWrapper<List<AccessoriesResponseModel>>> {
        return storeRemoteDao.getBackgrounds()
    }


}